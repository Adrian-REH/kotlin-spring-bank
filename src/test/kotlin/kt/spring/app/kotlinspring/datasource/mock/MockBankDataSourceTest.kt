package kt.spring.app.kotlinspring.datasource.mock

import kt.spring.app.kotlinspring.model.Bank
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.test.annotation.DirtiesContext

internal class MockBankDataSourceTest{

    private val mockDataSource = MockBankDataSource()

    @Nested
    @DisplayName("get Banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks{
        @Test
        fun `should provide a collection of banks`(){

            // given

            // when
            val banks = mockDataSource.getBanks()

            // then
            assertThat(banks).isNotEmpty
            assertThat(banks.size).isGreaterThanOrEqualTo(3)

        }
        @Test
        fun `should provide some mock data`(){

            // when
            val banks = mockDataSource.getBanks()

            // then
            assertThat(banks).allMatch { it.accountNumber.isNotBlank() && !it.trust.isNaN() }
            assertThat(banks).allMatch { !it.trust.isNaN() || it.trust != 0.0 }
            assertThat(banks).allMatch { it.transactionFee != 0 }
        }
    }
    @Nested
    @DisplayName("get Bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank{
        @Test
        fun `should provide a one bank`(){

            // given
            val accountNumber = "Hello"

            // when
            val bank = mockDataSource.getBank(accountNumber)

            // then
            assertThat(bank).matches { it.isPresent }
            assertThat(bank.get()).matches { it.accountNumber.isNotBlank() && !it.trust.isNaN() }
            assertThat(bank.get()).matches { !it.trust.isNaN() || it.trust != 0.0 }
            assertThat(bank.get()).matches { it.transactionFee != 0 }

        }
        @Test
        fun `should provide a not exist bank`(){
            // given
            val accountNumber = "s"
            // when
            val bank = mockDataSource.getBank(accountNumber)

            // then
            assertThat(bank).matches { it.isEmpty }
        }
    }

    @Nested
    @DisplayName("set Bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SetBank{
        @Test
        fun `should add to new bank`(){

            // given
            val newBank = Bank("Domain",0.9,8)

            // when
            val bank = mockDataSource.setBanks(newBank)

            // then
            assertThat(bank).matches { it.accountNumber.isNotBlank() && it.accountNumber == newBank.accountNumber }
            assertThat(bank).matches { !it.trust.isNaN() || it.trust ==0.9 }
            assertThat(bank).matches { it.transactionFee == 8 }

        }
        @Test
        fun `should add exist bank`(){
            val newBank = Bank("Jejeje",0.9,8)

            // when
            val bank = mockDataSource.setBanks(newBank)

            // then
            assertThat(bank).matches { it.accountNumber.isNotBlank() && it.accountNumber == newBank.accountNumber }
            assertThat(bank).matches { !it.trust.isNaN() || it.trust ==0.9 }
            assertThat(bank).matches { it.transactionFee == 8 }
        }
    }

    @Nested
    @DisplayName("update Bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateBank{
        @Test
        fun `should not update to new bank`(){

            // given
            val newBank = Bank("Domain",0.9,8)

            // when
            val bank = mockDataSource.updateBank(newBank)

            // then
            assertThat(bank).matches { it.isEmpty }

        }
        @Test
        fun `should update exist bank`(){
            val newBank = Bank("Jejeje",0.9,8)

            // when
            val bank = mockDataSource.updateBank(newBank)

            // then
            assertThat(bank.get()).matches { it.accountNumber.isNotBlank() && it.accountNumber == newBank.accountNumber }
            assertThat(bank.get()).matches { !it.trust.isNaN() || it.trust ==0.9 }
            assertThat(bank.get()).matches { it.transactionFee == 8 }
        }
    }

    @Nested
    @DisplayName("delete Bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank{
        @Test
        fun `should not delete the new bank`(){

            // given
            val accountNumber = "Domain"

            // when
            val bank = mockDataSource.deleteBank(accountNumber)

            // then
            assertThat(bank).matches { it==false}

        }
        @Test
        @DirtiesContext
        fun `should delete the exist bank`(){
            // given
            val accountNumber = "Jejeje"

            // when
            val bank = mockDataSource.deleteBank(accountNumber)

            // then
            assertThat(bank).matches { it==true}
        }
    }
}