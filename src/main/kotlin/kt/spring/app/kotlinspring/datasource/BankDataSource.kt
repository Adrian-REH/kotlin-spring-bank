package kt.spring.app.kotlinspring.datasource

import kt.spring.app.kotlinspring.model.Bank
import java.util.*

interface BankDataSource {

    fun getBanks(): Collection<Bank>
    fun getBank(accountNumber: String): Optional<Bank>
    fun setBanks(bank: Bank): Bank
    fun updateBank(bank: Bank): Optional<Bank>
    fun deleteBank(accountNumber: String): Boolean
}