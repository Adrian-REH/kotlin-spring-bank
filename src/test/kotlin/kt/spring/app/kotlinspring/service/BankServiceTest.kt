package kt.spring.app.kotlinspring.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kt.spring.app.kotlinspring.datasource.BankDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest{

    private val dataSource: BankDataSource = mockk(relaxed = true)
    private val bankService: BankService = BankService(dataSource)

    @Test
    fun  `should call its data source to retrieve banks`(){
        // given

        every { dataSource.getBanks() } returns emptyList()

        val banks = bankService.getBanks()
        verify(exactly = 1) { dataSource.getBanks() }


    }

}