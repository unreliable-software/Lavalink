package lavalink.plugin

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("test")
@Component
class TestProperties {
    var foo: String = ""
}