package kt.spring.app.kotlinspring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kt.spring.app.kotlinspring.model.Bank
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.transaction.AfterTransaction
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.Base64Utils
import org.springframework.util.MimeTypeUtils


@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest @Autowired constructor(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
){
    val baseUrl = "/api/banks"


    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks{

        @Test
        fun `should return all banks`(){
            mockMvc.perform(get("/api/banks")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.[0].account_number").value("Hello"))
                .andExpect(jsonPath("$.length()").value(3))


        }
    }


    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank{
        @Test
        fun `should return one bank`(){
            mockMvc.perform(get("$baseUrl/Hello")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.account_number").value("Hello"))

        }
        @Test
        fun `should return Not found bank`(){
            val accountNumber ="UNKNOWN"
            mockMvc.perform(get("$baseUrl/$accountNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$").value("Could not find a bank with account number $accountNumber"))

        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class SetBank{

        @Test
        fun `should add the new bank`() {
            val newBank = Bank("acc123",31.415,2)
            val payload: String = objectMapper.writeValueAsString(newBank);

            mockMvc.perform (post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
                .content(payload)
            )
                .andExpect ( status().isCreated )
                .andExpect ( jsonPath("$.account_number").value(newBank.accountNumber))
        }

        @Test
        fun `should add the exist bank`() {
            val existBank = Bank("Jejeje",0.4,4)
            val payload: String = objectMapper.writeValueAsString(existBank);

            mockMvc.perform (post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
                .content(payload)
                .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect ( status().isConflict )
                .andExpect ( jsonPath("$").value("Bank with account number ${existBank.accountNumber} already exists.") )

        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class UpdateBank{
        @Test
        fun `should update an existing bank`() {
            val newBank = Bank("Jejeje",56.415,2)
            val payload: String = objectMapper.writeValueAsString(newBank);

            mockMvc.perform(patch(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
                .content(payload)
                .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect ( status().isOk )
                .andExpect ( jsonPath("$.account_number").value(newBank.accountNumber))
        }

        @Test
        fun `should update an not existing bank`() {
            val newBank = Bank("acc1232",56.415,2)
            val payload: String = objectMapper.writeValueAsString(newBank);

            mockMvc.perform(patch(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
                .content(payload)
            )
                .andExpect ( status().isBadRequest )
                .andExpect ( jsonPath("$").value("Could not find a bank with account number ${newBank.accountNumber}"))
        }
    }


    @Nested
    @DisplayName("DELETE /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteBank{
        @Test
        @DirtiesContext // Cumple con Isolate, pero reconstruye los test
        fun `should delete an existing bank`() {
            val accountNumber ="Jejeje"

            mockMvc.perform(
                delete("$baseUrl/$accountNumber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect ( status().isOk )
                .andExpect ( jsonPath("$").value(true))
        }

        @Test
        fun `should delete an not existing bank`() {

            val accountNumber ="jojojo"
            mockMvc.perform(
                delete("$baseUrl/$accountNumber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("client:client".toByteArray()))
            )
                .andExpect ( status().isBadRequest )
                .andExpect ( jsonPath("$").value("Could not find a bank with account number ${accountNumber}"))
        }
    }

}