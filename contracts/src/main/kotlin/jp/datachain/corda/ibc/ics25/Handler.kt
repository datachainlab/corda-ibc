package jp.datachain.corda.ibc.ics25

import jp.datachain.corda.ibc.ics2.ClientState
import jp.datachain.corda.ibc.ics2.ClientType
import jp.datachain.corda.ibc.ics2.ConsensusState
import jp.datachain.corda.ibc.ics24.Host
import jp.datachain.corda.ibc.clients.corda.CordaClientState
import jp.datachain.corda.ibc.clients.corda.CordaConsensusState
import jp.datachain.corda.ibc.ics23.CommitmentPrefix
import jp.datachain.corda.ibc.ics23.CommitmentProof
import jp.datachain.corda.ibc.ics24.Identifier
import jp.datachain.corda.ibc.ics3.ConnectionEnd
import jp.datachain.corda.ibc.ics3.ConnectionState
import jp.datachain.corda.ibc.ics4.*
import jp.datachain.corda.ibc.states.Channel
import jp.datachain.corda.ibc.states.Connection
import jp.datachain.corda.ibc.types.Height
import jp.datachain.corda.ibc.types.Quadruple
import jp.datachain.corda.ibc.types.Version

object Handler {
    fun Host.createClient(
            id: Identifier,
            clientType: ClientType,
            consensusState: ConsensusState
    ): Pair<Host, ClientState> {
        when (clientType) {
            ClientType.CordaClient -> {
                val host = addClient(id)
                val consensusState = consensusState as CordaConsensusState
                val client = CordaClientState(host, id, consensusState)
                return Pair(host, client)
            }
            else -> throw NotImplementedError()
        }
    }

    fun Pair<Host, ClientState>.connOpenInit(
            identifier: Identifier,
            desiredCounterpartyConnectionIdentifier: Identifier,
            counterpartyPrefix: CommitmentPrefix,
            clientIdentifier: Identifier,
            counterpartyClientIdentifier: Identifier
    ) : Triple<Host, ClientState, Connection> {
        val host = this.first.addConnection(identifier)
        val client = this.second.addConnection(identifier)

        require(host.clientIds.contains(client.id)){"unknown client"}
        require(clientIdentifier == client.id){"mismatch client"}

        val end = ConnectionEnd(
                ConnectionState.INIT,
                desiredCounterpartyConnectionIdentifier,
                counterpartyPrefix,
                clientIdentifier,
                counterpartyClientIdentifier,
                host.getCompatibleVersions()
        )

        return Triple(host, client, Connection(host, identifier, end))
    }

    fun Triple<Host, ClientState, Connection?>.connOpenTry(
            desiredIdentifier: Identifier,
            counterpartyConnectionIdentifier: Identifier,
            counterpartyPrefix: CommitmentPrefix,
            counterpartyClientIdentifier: Identifier,
            clientIdentifier: Identifier,
            counterpartyVersions: Version.Multiple,
            proofInit: CommitmentProof,
            proofConsensus: CommitmentProof,
            proofHeight: Height,
            consensusHeight: Height
    ) : Triple<Host, ClientState, Connection> {
        val previous = this.third
        val (host, client) = if (previous == null) {
            Pair(this.first.addConnection(desiredIdentifier), this.second.addConnection(desiredIdentifier))
        } else {
            require(this.first.connIds.contains(desiredIdentifier)){"unknown connection in host"}
            require(this.second.connIds.contains(desiredIdentifier)){"unknown connection in client"}
            require(previous.id == desiredIdentifier){"mismatch connection"}
            Pair(this.first, this.second)
        }

        require(host.clientIds.contains(client.id)){"unknown client"}
        require(clientIdentifier == client.id){"mismatch client"}

        val expected = ConnectionEnd(
                ConnectionState.INIT,
                desiredIdentifier,
                host.getCommitmentPrefix(),
                counterpartyClientIdentifier,
                clientIdentifier,
                counterpartyVersions)
        require(client.verifyConnectionState(
                proofHeight,
                counterpartyPrefix,
                proofInit,
                counterpartyConnectionIdentifier,
                expected)){"connection verification failure"}

        val expectedConsensusState = host.getConsensusState(consensusHeight)
        require(client.verifyClientConsensusState(
                proofHeight,
                counterpartyPrefix,
                proofConsensus,
                counterpartyClientIdentifier,
                consensusHeight,
                expectedConsensusState)){"client consensus verification failure"}

        val version = host.pickVersion(counterpartyVersions)
        val connectionEnd = ConnectionEnd(
                ConnectionState.TRYOPEN,
                counterpartyConnectionIdentifier,
                counterpartyPrefix,
                clientIdentifier,
                counterpartyClientIdentifier,
                version)
        require(previous == null ||
                (previous.end.state == ConnectionState.INIT &&
                        previous.end.counterpartyConnectionIdentifier == counterpartyConnectionIdentifier &&
                        previous.end.counterpartyPrefix == counterpartyPrefix &&
                        previous.end.clientIdentifier == clientIdentifier &&
                        previous.end.counterpartyClientIdentifier == counterpartyClientIdentifier &&
                        previous.end.version == version)){"invalid previous state"}

        return Triple(host, client, Connection(host, desiredIdentifier, connectionEnd))
    }

    fun Triple<Host, ClientState, Connection>.connOpenAck(
            identifier: Identifier,
            version: Version.Single,
            proofTry: CommitmentProof,
            proofConsensus: CommitmentProof,
            proofHeight: Height,
            consensusHeight: Height
    ) : Connection {
        val host = this.first
        val client = this.second
        val conn = this.third

        require(host.clientIds.contains(client.id)){"unknown client"}
        require(host.connIds.contains(conn.id)){"unknown connection in host"}
        require(client.connIds.contains(conn.id)){"unknown connection in client"}
        require(conn.id == identifier){"mismatch connection"}

        require(consensusHeight.height <= host.getCurrentHeight().height){"unknown height"}
        require(conn.end.state == ConnectionState.INIT || conn.end.state == ConnectionState.TRYOPEN){"invalid connection state"}

        val expected = ConnectionEnd(
                ConnectionState.TRYOPEN,
                identifier,
                host.getCommitmentPrefix(),
                conn.end.counterpartyClientIdentifier,
                conn.end.clientIdentifier,
                version)
        require(client.verifyConnectionState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofTry,
                conn.end.counterpartyConnectionIdentifier,
                expected)){"connection verification failure"}

        val expectedConsensusState = host.getConsensusState(consensusHeight)
        require(client.verifyClientConsensusState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofConsensus,
                conn.end.counterpartyClientIdentifier,
                consensusHeight,
                expectedConsensusState)){"client consensus verification failure"}

        require(host.getCompatibleVersions().versions.contains(version.version)){"incompatible version"}

        return conn.copy(end = conn.end.copy(state = ConnectionState.OPEN,  version = version))
    }

    fun Triple<Host, ClientState, Connection>.connOpenConfirm(
            identifier: Identifier,
            proofAck: CommitmentProof,
            proofHeight: Height
    ) : Connection {
        val host = this.first
        val client = this.second
        val conn = this.third

        require(host.clientIds.contains(client.id)){"unknown client"}
        require(host.connIds.contains(conn.id)){"unknown connection in host"}
        require(client.connIds.contains(conn.id)){"unknown connection in client"}
        require(conn.id == identifier){"mismatch connection"}

        require(conn.end.state == ConnectionState.TRYOPEN){"invalid connection state"}
        val expected = ConnectionEnd(
                ConnectionState.OPEN,
                identifier,
                host.getCommitmentPrefix(),
                conn.end.counterpartyClientIdentifier,
                conn.end.clientIdentifier,
                conn.end.version)
        require(client.verifyConnectionState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofAck,
                conn.end.counterpartyConnectionIdentifier,
                expected)){"connection verification failure"}

        return conn.copy(end = conn.end.copy(state = ConnectionState.OPEN))
    }

    fun Pair<Host, Connection>.chanOpenInit(
            order: ChannelOrder,
            connectionHops: List<Identifier>,
            portIdentifier: Identifier,
            channelIdentifier: Identifier,
            counterpartyPortIdentifier: Identifier,
            counterpartyChannelIdentifier: Identifier,
            version: Version.Single
    ) : Pair<Host, Channel> {
        // TODO: port authentication should be added somehow

        val host = this.first.addPortChannel(portIdentifier, channelIdentifier)
        val conn = this.second

        require(host.connIds.contains(conn.id))

        require(conn.id == connectionHops.single())

        val end = ChannelEnd(
                ChannelState.INIT,
                order,
                counterpartyPortIdentifier,
                counterpartyChannelIdentifier,
                connectionHops,
                version)

        return Pair(host, Channel(host, portIdentifier, channelIdentifier, end))
    }

    fun Quadruple<Host, ClientState, Connection, Channel?>.chanOpenTry(
            order: ChannelOrder,
            connectionHops: List<Identifier>,
            portIdentifier: Identifier,
            channelIdentifier: Identifier,
            counterpartyPortIdentifier: Identifier,
            counterpartyChannelIdentifier: Identifier,
            version: Version.Single,
            counterpartyVersion: Version.Single,
            proofInit: CommitmentProof,
            proofHeight: Height
    ) : Pair<Host, Channel> {
        val previous = this.fourth
        val host = if (previous == null) {
            this.first.addPortChannel(portIdentifier, channelIdentifier)
        } else {
            require(this.first.portChanIds.contains(Pair(portIdentifier, channelIdentifier)))
            require(previous.portId == portIdentifier)
            require(previous.id == channelIdentifier)
            this.first
        }
        val client = this.second
        val conn = this.third

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(client.connIds.contains(conn.id))
        require(conn.id == connectionHops.single())

        require(previous == null ||
                ( previous.end.state == ChannelState.INIT &&
                        previous.end.ordering == order &&
                        previous.end.counterpartyPortIdentifier == counterpartyPortIdentifier &&
                        previous.end.counterpartyChannelIdentifier == counterpartyChannelIdentifier &&
                        previous.end.connectionHops == connectionHops &&
                        previous.end.version == version))

        require(conn.end.state == ConnectionState.OPEN)

        val expected = ChannelEnd(
                ChannelState.INIT,
                order,
                portIdentifier,
                channelIdentifier,
                listOf(conn.end.counterpartyConnectionIdentifier),
                counterpartyVersion)
        require(client.verifyChannelState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofInit,
                counterpartyPortIdentifier,
                counterpartyChannelIdentifier,
                expected))

        val end = ChannelEnd(
                ChannelState.TRYOPEN,
                order,
                counterpartyPortIdentifier,
                counterpartyChannelIdentifier,
                connectionHops,
                version)

        return Pair(host, Channel(host, portIdentifier, channelIdentifier, end))
    }

    fun Quadruple<Host, ClientState, Connection, Channel>.chanOpenAck(
            portIdentifier: Identifier,
            channelIdentifier: Identifier,
            counterpartyVersion: Version.Single,
            proofTry: CommitmentProof,
            proofHeight: Height
    ) : Channel {
        val host = this.first
        val client = this.second
        val conn = this.third
        val chan = this.fourth

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(client.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(chan.portId == portIdentifier)
        require(chan.id == channelIdentifier)

        require(chan.end.state == ChannelState.INIT || chan.end.state == ChannelState.TRYOPEN)

        require(conn.id == chan.end.connectionHops.single())

        require(conn.end.state == ConnectionState.OPEN)

        val expected = ChannelEnd(
                ChannelState.TRYOPEN,
                chan.end.ordering,
                portIdentifier,
                channelIdentifier,
                listOf(conn.end.counterpartyConnectionIdentifier),
                counterpartyVersion)
        require(client.verifyChannelState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofTry,
                chan.end.counterpartyPortIdentifier,
                chan.end.counterpartyChannelIdentifier,
                expected))

        return chan.copy(end = chan.end.copy(state = ChannelState.OPEN, version = counterpartyVersion))
    }

    fun Quadruple<Host, ClientState, Connection, Channel>.chanOpenConfirm(
            portIdentifier: Identifier,
            channelIdentifier: Identifier,
            proofAck: CommitmentProof,
            proofHeight: Height
    ) : Channel {
        val host = this.first
        val client = this.second
        val conn = this.third
        val chan = this.fourth

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(client.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(chan.portId == portIdentifier)
        require(chan.id == channelIdentifier)

        require(chan.end.state == ChannelState.TRYOPEN)

        require(conn.id == chan.end.connectionHops.single())

        require(conn.end.state == ConnectionState.OPEN)

        val expected = ChannelEnd(
                ChannelState.OPEN,
                chan.end.ordering,
                portIdentifier,
                channelIdentifier,
                listOf(conn.end.counterpartyConnectionIdentifier),
                chan.end.version)
        require(client.verifyChannelState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofAck,
                chan.end.counterpartyPortIdentifier,
                chan.end.counterpartyChannelIdentifier,
                expected))

        return chan.copy(end = chan.end.copy(state = ChannelState.OPEN))
    }

    fun Triple<Host, Connection, Channel>.chanCloseInit(
            portIdentifier: Identifier,
            channelIdentifier: Identifier
    ) : Channel {
        val host = this.first
        val conn = this.second
        val chan = this.third

        require(host.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(chan.portId == portIdentifier)
        require(chan.id == channelIdentifier)

        require(chan.end.state != ChannelState.CLOSED)

        require(conn.id == chan.end.connectionHops.single())

        require(conn.end.state == ConnectionState.OPEN)

        return chan.copy(end = chan.end.copy(state = ChannelState.CLOSED))
    }

    fun Quadruple<Host, ClientState, Connection, Channel>.chanCloseConfirm(
            portIdentifier: Identifier,
            channelIdentifier: Identifier,
            proofInit: CommitmentProof,
            proofHeight: Height
    ) : Channel {
        val host = this.first
        val client = this.second
        val conn = this.third
        val chan = this.fourth

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(client.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(chan.portId == portIdentifier)
        require(chan.id == channelIdentifier)

        require(chan.end.state != ChannelState.CLOSED)

        require(conn.id == chan.end.connectionHops.single())

        require(conn.end.state == ConnectionState.OPEN)

        val expected = ChannelEnd(
                ChannelState.CLOSED,
                chan.end.ordering,
                portIdentifier,
                channelIdentifier,
                listOf(conn.end.counterpartyConnectionIdentifier),
                chan.end.version)
        require(client.verifyChannelState(
                proofHeight,
                conn.end.counterpartyPrefix,
                proofInit,
                chan.end.counterpartyPortIdentifier,
                chan.end.counterpartyChannelIdentifier,
                expected))

        return chan.copy(end = chan.end.copy(state = ChannelState.CLOSED))
    }

    fun Quadruple<Host, ClientState, Connection, Channel>.sendPacket(packet: Packet) : Channel {
        val host = this.first
        val client = this.second
        val conn = this.third
        val chan = this.fourth

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(client.connIds.contains(conn.id))

        require(chan.end.state != ChannelState.CLOSED)

        require(packet.sourcePort == chan.portId)
        require(packet.sourceChannel == chan.id)
        require(packet.destPort == chan.end.counterpartyPortIdentifier)
        require(packet.destChannel == chan.end.counterpartyChannelIdentifier)

        require(conn.id == chan.end.connectionHops.single())

        require(conn.end.clientIdentifier == client.id)
        val latestClientHeight = client.latestClientHeight()
        require(packet.timeoutHeight.height == 0 || latestClientHeight.height < packet.timeoutHeight.height)

        require(packet.sequence == chan.nextSequenceSend)

        return chan.copy(
                nextSequenceSend = chan.nextSequenceSend + 1,
                packets = chan.packets + mapOf(packet.sequence to packet))
    }

    fun Quadruple<Host, ClientState, Connection, Channel>.recvPacket(
            packet: Packet,
            proof: CommitmentProof,
            proofHeight: Height,
            acknowledgement: Acknowledgement
    ) : Channel {
        val host = this.first
        val client = this.second
        val conn = this.third
        var chan = this.fourth

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(client.connIds.contains(conn.id))

        require(packet.destPort == chan.portId)
        require(packet.destChannel == chan.id)

        require(chan.end.state == ChannelState.OPEN)
        require(packet.sourcePort == chan.end.counterpartyPortIdentifier)
        require(packet.sourceChannel == chan.end.counterpartyChannelIdentifier)

        require(conn.id == chan.end.connectionHops.single())
        require(conn.end.state == ConnectionState.OPEN)

        require(packet.timeoutHeight.height == 0 || host.getCurrentHeight().height < packet.timeoutHeight.height)
        require(packet.timeoutTimestamp.timestamp == 0 || host.currentTimestamp().timestamp < packet.timeoutTimestamp.timestamp)

        require(client.verifyPacketData(
                proofHeight,
                conn.end.counterpartyPrefix,
                proof,
                packet.sourcePort,
                packet.sourceChannel,
                packet.sequence,
                packet))

        if (acknowledgement.data.size > 0 || chan.end.ordering == ChannelOrder.UNORDERED) {
            chan = chan.copy(acknowledgements = chan.acknowledgements + mapOf(packet.sequence to acknowledgement))
        }

        if (chan.end.ordering == ChannelOrder.ORDERED) {
            require(packet.sequence == chan.nextSequenceRecv)
            chan = chan.copy(nextSequenceRecv = chan.nextSequenceRecv + 1)
        }

        return chan
    }

    fun Quadruple<Host, ClientState, Connection, Channel>.acknowledgePacket(
            packet: Packet,
            acknowledgement: Acknowledgement,
            proof: CommitmentProof,
            proofHeight: Height
    ) : Channel {
        val host = this.first
        val client = this.second
        val conn = this.third
        var chan = this.fourth

        require(host.clientIds.contains(client.id))
        require(host.connIds.contains(conn.id))
        require(host.portChanIds.contains(Pair(chan.portId, chan.id)))
        require(client.connIds.contains(conn.id))

        require(packet.sourcePort == chan.portId)
        require(packet.sourceChannel == chan.id)

        require(chan.end.state == ChannelState.OPEN)

        require(packet.destPort == chan.end.counterpartyPortIdentifier)
        require(packet.destChannel == chan.end.counterpartyChannelIdentifier)

        require(conn.id == chan.end.connectionHops.single())
        require(conn.end.state == ConnectionState.OPEN)

        require(chan.packets[packet.sequence] == packet)

        require(client.verifyPacketAcknowledgement(
                proofHeight,
                conn.end.counterpartyPrefix,
                proof,
                packet.destPort,
                packet.destChannel,
                packet.sequence,
                acknowledgement))

        if (chan.end.ordering == ChannelOrder.ORDERED) {
            require(packet.sequence == chan.nextSequenceAck)
            chan = chan.copy(nextSequenceAck = chan.nextSequenceAck + 1)
        }

        chan = chan.copy(packets = chan.packets - packet.sequence)
        return chan
    }
}