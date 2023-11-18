package kt.spring.app.kotlinspring.datasource.mock

import kt.spring.app.kotlinspring.datasource.BankDataSource
import kt.spring.app.kotlinspring.model.Bank
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository("local")
class MockBankDataSource: BankDataSource {


    val banks = mutableListOf(
        Bank("Hello",0.1,2),
        Bank("World",0.2,3),
        Bank("Jejeje",0.4,4),

        )
    override fun getBanks(): Collection<Bank> = banks

    override fun getBank(accountNumber: String): Optional<Bank> {
        return Optional.ofNullable(banks.firstOrNull { it.accountNumber == accountNumber })
    }

    override fun setBanks(bank: Bank): Bank {
        banks.add(bank)
        return bank;
    }

    override fun updateBank(bank: Bank): Optional<Bank> {
        banks.indexOfFirst { it.accountNumber == bank.accountNumber }.takeIf { it != -1 }?.let { index ->
            banks[index] = bank
            return Optional.of(bank)
        }

        return Optional.empty()
    }

    override fun deleteBank(accountNumber: String): Boolean {
        return banks.removeIf { it.accountNumber == accountNumber }
    }
}