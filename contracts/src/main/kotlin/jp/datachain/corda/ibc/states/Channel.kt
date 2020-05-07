package jp.datachain.corda.ibc.states

import jp.datachain.corda.ibc.contracts.Ibc
import jp.datachain.corda.ibc.ics24.Host
import jp.datachain.corda.ibc.ics24.Identifier
import jp.datachain.corda.ibc.ics4.Acknowledgement
import jp.datachain.corda.ibc.ics4.ChannelEnd
import jp.datachain.corda.ibc.ics4.Packet
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty

@BelongsToContract(Ibc::class)
data class Channel private constructor (
        override val participants: List<AbstractParty>,
        override val linearId: UniqueIdentifier,
        val portId: Identifier,
        val end: ChannelEnd,
        val nextSequenceSend: Int,
        val nextSequenceRecv: Int,
        val nextSequenceAck: Int,
        val packets: Map<Int, Packet>,
        val acknowledgements: Map<Int, Acknowledgement>
) : IbcState {
    constructor(host: Host, portId: Identifier, chanId: Identifier, end: ChannelEnd) : this(
            host.participants,
            chanId.toUniqueIdentifier(),
            portId,
            end,
            1,
            1,
            1,
            emptyMap(),
            emptyMap())
}
