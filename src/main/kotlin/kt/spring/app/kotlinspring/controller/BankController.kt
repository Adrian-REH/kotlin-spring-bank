package kt.spring.app.kotlinspring.controller

import kt.spring.app.kotlinspring.model.Bank
import kt.spring.app.kotlinspring.service.BankService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam
@Configuration
class SecurityConfig {

    @Bean
    fun csrfTokenRepository(): HttpSessionCsrfTokenRepository {
        return HttpSessionCsrfTokenRepository()
    }

    @Configuration
    class WebSecurityConfig : WebSecurityConfigurerAdapter() {

        override fun configure(http: HttpSecurity) {
            http.cors().and().csrf().disable();

            // Otras configuraciones de seguridad aquí
        }
    }
}
@RestController
@RequestMapping("/api/banks")
class BankController(private val bankService: BankService){

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> = ResponseEntity(e.message,HttpStatus.BAD_REQUEST)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArg(e: IllegalArgumentException): ResponseEntity<String> = ResponseEntity(e.message,HttpStatus.CONFLICT)


    @GetMapping
    fun findAllBank():Collection<Bank> = bankService.getBanks()

    @GetMapping("/{accountNumber}")
    fun findOneBank(@PathVariable accountNumber: String):Bank {
        return bankService.getBank(accountNumber)
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody bank: Bank):Bank {
        return bankService.setBank(bank)
    }
    @PatchMapping
    fun updateBank(@RequestBody bank: Bank):Bank {
        return bankService.updateBank(bank)
    }
    @DeleteMapping("/{accountNumber}")
    fun deleteBank(@PathVariable accountNumber: String):Boolean {
        return bankService.deleteBank(accountNumber)
    }
}