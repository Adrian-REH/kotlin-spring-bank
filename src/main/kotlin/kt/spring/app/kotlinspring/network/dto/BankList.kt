package kt.spring.app.kotlinspring.network.dto

import kt.spring.app.kotlinspring.model.Bank

data class BankList (
    val results: Collection<Bank>
)