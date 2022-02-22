package lavalink.server.integration

import lavalink.plugin.TestProperties
import lavalink.server.util.SharedSpringContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SharedSpringContext::class)
class PropertiesTest {

    @Test
    fun configuratorTest(props: TestProperties) {
        Assertions.assertEquals("bar", props.foo)
    }

}
