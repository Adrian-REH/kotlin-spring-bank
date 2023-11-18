package kt.spring.app.kotlinspring.service

import kt.spring.app.kotlinspring.datasource.BankDataSource
import kt.spring.app.kotlinspring.model.Bank
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import javax.swing.text.StyledEditorKit.BoldAction

@Service
class BankService(@Qualifier("local")private val dataSource: BankDataSource) {


    fun getBanks(): Collection<Bank> {
        return dataSource.getBanks()
    }
    fun getBank(accountNumber: String): Bank {
        return findBank(accountNumber)
    }

    fun setBank(bank: Bank): Bank {
        if (dataSource.getBanks().any{it.accountNumber == bank.accountNumber}){
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists.")
        }

        return dataSource.setBanks(bank)
    }

    fun updateBank(bank: Bank): Bank {
        val result = dataSource.updateBank(bank)
        if (result.isEmpty) throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")
        return result.get()

    }

    fun deleteBank(accountNumber: String): Boolean {
        findBank(accountNumber)
        return dataSource.deleteBank(accountNumber)

    }

    private fun findBank(accountNumber: String): Bank {
        val result = dataSource.getBank( accountNumber)
        if (result.isEmpty) throw NoSuchElementException("Could not find a bank with account number $accountNumber")
        return result.get()
    }

}