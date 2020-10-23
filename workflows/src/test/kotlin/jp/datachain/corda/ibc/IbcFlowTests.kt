package jp.datachain.corda.ibc

import jp.datachain.corda.ibc.flows.*
import jp.datachain.corda.ibc.ics2.ClientType
import jp.datachain.corda.ibc.ics24.Identifier
import jp.datachain.corda.ibc.ics4.Acknowledgement
import jp.datachain.corda.ibc.ics4.ChannelOrder
import jp.datachain.corda.ibc.ics4.Packet
import jp.datachain.corda.ibc.types.Height
import jp.datachain.corda.ibc.types.Timestamp
import jp.datachain.corda.ibc.types.Version
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.OpaqueBytes
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkNotarySpec
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test

class IbcFlowTests {
    private val networkParam = MockNetworkParameters(
            cordappsForAllNodes = listOf(
                    TestCordapp.findCordapp("jp.datachain.corda.ibc.contracts"),
                    TestCordapp.findCordapp("jp.datachain.corda.ibc.flows")
            ),
            notarySpecs = listOf(
                    MockNetworkNotarySpec(CordaX500Name("My Mock Notary Service", "Kawasaki", "JP"), validating = true)
            )
    )
    private val network = MockNetwork(networkParam.withNetworkParameters(networkParam.networkParameters.copy(minimumPlatformVersion = 4)))
    private val a = network.createNode()
    private val b = network.createNode()
    private val c = network.createNode()
    private val x = network.createNode()
    private val y = network.createNode()
    private val z = network.createNode()

    init {
        listOf(a, b, c, x, y, z).forEach {
            it.registerInitiatedFlow(IbcGenesisCreateResponderFlow::class.java)
            it.registerInitiatedFlow(IbcHostAndBankCreateResponderFlow::class.java)
            it.registerInitiatedFlow(IbcFundAllocateResponderFlow::class.java)

            it.registerInitiatedFlow(IbcClientCreateResponderFlow::class.java)

            it.registerInitiatedFlow(IbcConnOpenInitResponderFlow::class.java)
            it.registerInitiatedFlow(IbcConnOpenTryResponderFlow::class.java)
            it.registerInitiatedFlow(IbcConnOpenAckResponderFlow::class.java)
            it.registerInitiatedFlow(IbcConnOpenConfirmResponderFlow::class.java)

            it.registerInitiatedFlow(IbcChanOpenInitResponderFlow::class.java)
            it.registerInitiatedFlow(IbcChanOpenTryResponderFlow::class.java)
            it.registerInitiatedFlow(IbcChanOpenAckResponderFlow::class.java)
            it.registerInitiatedFlow(IbcChanOpenConfirmResponderFlow::class.java)
            it.registerInitiatedFlow(IbcChanCloseInitResponderFlow::class.java)
            it.registerInitiatedFlow(IbcChanCloseConfirmResponderFlow::class.java)

            it.registerInitiatedFlow(IbcSendPacketResponderFlow::class.java)
            it.registerInitiatedFlow(IbcRecvPacketResponderFlow::class.java)
            it.registerInitiatedFlow(IbcAcknowledgePacketResponderFlow::class.java)
        }
    }

    @Before
    fun setup() = network.runNetwork()

    @After
    fun tearDown() = network.stopNodes()

    @Test
    fun `relayer logic`() {
        val ibcA = TestCordaIbcClient(network, a)
        val ibcB = TestCordaIbcClient(network, x)

        ibcA.createHost(listOf(
                a.info.legalIdentities.single(),
                b.info.legalIdentities.single(),
                c.info.legalIdentities.single()
        ))
        ibcB.createHost(listOf(
                x.info.legalIdentities.single(),
                y.info.legalIdentities.single(),
                z.info.legalIdentities.single()
        ))

        val clientAid = Identifier("clientA")
        val consensusStateB = ibcB.host().getConsensusState(Height(0))
        ibcA.createClient(clientAid, ClientType.CordaClient, consensusStateB)

        val clientBid = Identifier("clientB")
        val consensusStateA = ibcA.host().getConsensusState(Height(0))
        ibcB.createClient(clientBid, ClientType.CordaClient, consensusStateA)

        val connAid = Identifier("connectionA")
        val connBid = Identifier("connectionB")
        ibcA.connOpenInit(
                connAid,
                connBid,
                ibcB.host().getCommitmentPrefix(),
                clientAid,
                clientBid)

        ibcB.connOpenTry(
                connBid,
                connAid,
                ibcA.host().getCommitmentPrefix(),
                clientAid,
                clientBid,
                ibcA.host().getCompatibleVersions(),
                ibcA.connProof(connAid),
                ibcA.clientProof(clientAid),
                ibcA.host().getCurrentHeight(),
                ibcB.host().getCurrentHeight())

        ibcA.connOpenAck(
                connAid,
                ibcB.conn(connBid).end.version as Version.Single,
                ibcB.connProof(connBid),
                ibcB.clientProof(clientBid),
                ibcB.host().getCurrentHeight(),
                ibcA.host().getCurrentHeight())

        ibcB.connOpenConfirm(
                connBid,
                ibcA.connProof(connAid),
                ibcA.host().getCurrentHeight())

        val portAid = Identifier("portA")
        val chanAid = Identifier("channelA")
        val portBid = Identifier("portB")
        val chanBid = Identifier("channelB")
        ibcA.chanOpenInit(
                ChannelOrder.ORDERED,
                listOf(connAid),
                portAid,
                chanAid,
                portBid,
                chanBid,
                ibcA.conn(connAid).end.version as Version.Single)

        ibcB.chanOpenTry(
                ChannelOrder.ORDERED,
                listOf(connBid),
                portBid,
                chanBid,
                portAid,
                chanAid,
                ibcB.conn(connBid).end.version as Version.Single,
                ibcA.conn(connAid).end.version as Version.Single,
                ibcA.chanProof(chanAid),
                ibcA.host().getCurrentHeight())

        ibcA.chanOpenAck(
                portAid,
                chanAid,
                ibcB.chan(chanBid).end.version,
                ibcB.chanProof(chanBid),
                ibcB.host().getCurrentHeight())

        ibcB.chanOpenConfirm(
                portBid,
                chanBid,
                ibcA.chanProof(chanAid),
                ibcA.host().getCurrentHeight())

        for (sequence in 1L..10) {
            val packet = Packet(
                    OpaqueBytes("Hello, Bob! (${sequence})".toByteArray()),
                    portAid,
                    chanAid,
                    portBid,
                    chanBid,
                    Height(0),
                    Timestamp(0),
                    sequence)
            ibcA.sendPacket(packet)

            val ack = Acknowledgement(OpaqueBytes("Thank you, Alice! (${sequence})".toByteArray()))
            ibcB.recvPacket(
                    packet,
                    ibcA.chanProof(chanAid),
                    ibcA.host().getCurrentHeight(),
                    ack)

            ibcA.acknowledgePacket(
                    packet,
                    ack,
                    ibcB.chanProof(chanBid),
                    ibcB.host().getCurrentHeight())
        }

        for (sequence in 1L..10) {
            val packet = Packet(
                    OpaqueBytes("Hello, Alice! (${sequence})".toByteArray()),
                    portBid,
                    chanBid,
                    portAid,
                    chanAid,
                    Height(0),
                    Timestamp(0),
                    sequence)
            ibcB.sendPacket(packet)

            val ack = Acknowledgement(OpaqueBytes("Thank you, Bob! (${sequence})".toByteArray()))
            ibcA.recvPacket(
                    packet,
                    ibcB.chanProof(chanBid),
                    ibcB.host().getCurrentHeight(),
                    ack)

            ibcB.acknowledgePacket(
                    packet,
                    ack,
                    ibcA.chanProof(chanAid),
                    ibcA.host().getCurrentHeight())
        }

        ibcA.chanCloseInit(
                portAid,
                chanAid)

        ibcB.chanCloseConfirm(
                portBid,
                chanBid,
                ibcA.chanProof(chanAid),
                ibcA.host().getCurrentHeight())
    }
}