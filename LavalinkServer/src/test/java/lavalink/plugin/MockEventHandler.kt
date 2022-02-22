package lavalink.plugin

import dev.arbjerg.lavalink.api.IPlayer
import dev.arbjerg.lavalink.api.ISocketContext
import dev.arbjerg.lavalink.api.PluginEventHandler
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class MockEventHandler : PluginEventHandler() {

    private val log: Logger = LoggerFactory.getLogger(MockEventHandler::class.java)
    val latch = CountDownLatch(6)
    private val triggered = mutableSetOf<String>()
    @Volatile
    var active = false

    fun onTrigger(type: String, context: ISocketContext) {
        synchronized(this) {
        if (!active || triggered.contains(type)) return
            triggered.add(type)
            log.info("Triggered $type")
            latch.countDown()
        }
        if (latch.count <= 1) {
            context.sendMessage(JSONObject().apply { put("op", "test") })
            context.closeWebSocket()
        }
    }

    override fun onDestroyPlayer(context: ISocketContext, player: IPlayer?) {
        onTrigger("onDestroyPlayer", context)
    }

    override fun onWebSocketOpen(context: ISocketContext, resumed: Boolean) {
        context.getPlayer(0)
        context.destroyPlayer(0)
        //context.sendMessage(JSONObject().apply { put("op", "test") })
        onTrigger("onWebSocketOpen", context)
    }

    override fun onNewPlayer(context: ISocketContext, player: IPlayer?) {
        onTrigger("onNewPlayer", context)
    }

    override fun onSocketContextDestroyed(context: ISocketContext) {
        onTrigger("onSocketContextDestroyed", context)
    }

    override fun onWebSocketMessageOut(context: ISocketContext, message: String?) {
        onTrigger("onWebSocketMessageOut", context)
    }

    override fun onWebsocketMessageIn(context: ISocketContext, message: String) {
        if(JSONObject(message).opt("op") != "test") return
        onTrigger("onWebsocketMessageIn", context)
    }
}