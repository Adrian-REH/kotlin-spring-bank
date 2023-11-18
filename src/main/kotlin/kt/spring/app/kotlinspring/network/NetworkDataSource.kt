package kt.spring.app.kotlinspring.network

import kt.spring.app.kotlinspring.datasource.BankDataSource
import kt.spring.app.kotlinspring.model.Bank
import kt.spring.app.kotlinspring.network.dto.BankList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException
import java.util.*

@Repository("network")
class NetworkDataSource(
    @Autowired private val restTemplate: RestTemplate
): BankDataSource {

    override fun getBanks(): Collection<Bank> {
        val response: ResponseEntity<BankList> = restTemplate.getForEntity<BankList>("")

        return response.body?.results?: throw IOException("Could not fetch banks from the network")
    }

    override fun getBank(accountNumber: String): Optional<Bank> {
        TODO("Not yet implemented")
    }

    override fun setBanks(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Optional<Bank> {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String): Boolean {
        TODO("Not yet implemented")
    }
}