package lavalink.plugin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MockEndpoint {

    @GetMapping("/mock")
    fun mock() = "Hello, world!"

}