package kt.spring.app.kotlinspring

import lombok.AllArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@AllArgsConstructor
@RestController
@RequestMapping("/intent")
class IntentController {


    @GetMapping("/login")
    fun login(): ResponseEntity<String?> {
        return ResponseEntity.ok<String>("Hello world!")
    }
}