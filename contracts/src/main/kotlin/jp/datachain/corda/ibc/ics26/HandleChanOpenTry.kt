package jp.datachain.corda.ibc.ics26

import ibc.core.client.v1.Client.Height
import ibc.core.connection.v1.Connection
import jp.datachain.corda.ibc.ics23.CommitmentProof
import jp.datachain.corda.ibc.ics24.Identifier
import jp.datachain.corda.ibc.ics25.Handler
import jp.datachain.corda.ibc.ics4.ChannelOrder
import java.security.PublicKey

data class HandleChanOpenTry(
        val order: ChannelOrder,
        val connectionHops: List<Identifier>,
        val portIdentifier: Identifier,
        val channelIdentifier: Identifier,
        val counterpartyChosenChannelIdentifer: Identifier,
        val counterpartyPortIdentifier: Identifier,
        val counterpartyChannelIdentifier: Identifier,
        val version: Connection.Version,
        val counterpartyVersion: Connection.Version,
        val proofInit: CommitmentProof,
        val proofHeight: Height
): DatagramHandler {
    override fun execute(ctx: Context, signers: Collection<PublicKey>) {
        Handler.chanOpenTry(
                ctx,
                order,
                connectionHops,
                portIdentifier,
                channelIdentifier,
                counterpartyChosenChannelIdentifer,
                counterpartyPortIdentifier,
                counterpartyChannelIdentifier,
                version,
                counterpartyVersion,
                proofInit,
                proofHeight)
    }
}