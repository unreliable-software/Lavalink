package lavalink.plugin

import dev.arbjerg.lavalink.api.ISocketContext
import dev.arbjerg.lavalink.api.WebSocketExtension
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class TestWsExtension : WebSocketExtension {

    val latch = CountDownLatch(1)
    override fun getOpName() = "extension-test"

    override fun onInvocation(context: ISocketContext, message: JSONObject) {
        latch.countDown()
    }


}