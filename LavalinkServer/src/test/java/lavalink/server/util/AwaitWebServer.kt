package lavalink.server.util

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.autoconfigure.web.ServerProperties
import java.net.Socket

class AwaitWebServer : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        val port = SpringContextProvider.staticContext!!.getBean(ServerProperties::class.java).port
        repeat(20) {
            try {
                Socket("localhost", port)
                return
            } catch (e: Exception) {
                Thread.sleep(100)
            }
        }
        throw RuntimeException("Web server not open")
    }
}