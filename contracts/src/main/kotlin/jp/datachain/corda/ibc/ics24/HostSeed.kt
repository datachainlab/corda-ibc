package jp.datachain.corda.ibc.ics24

import jp.datachain.corda.ibc.contracts.Ibc
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party

@BelongsToContract(Ibc::class)
data class HostSeed(override val participants: List<AbstractParty>, val notary: Party) : ContractState