package lavalink.server.util

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

class TestWsClient(uri: String, password: String): WebSocketAdapter() {

    private val socket: WebSocket
    private lateinit var sink: FluxSink<JSONObject>

    init {
         socket = WebSocketFactory().createSocket(uri)
             .addHeader("Authorization", password)
             .addHeader("User-Id", "0")
             .addHeader("Client-name", "MockClient")
             .addListener(this)
    }

    fun connect(postConnect: (() -> Unit)? = null): Flux<JSONObject> = Flux.create { sink ->
        this.sink = sink
        socket.connect()
        if (postConnect != null) postConnect()
    }

    fun send(json: JSONObject) {
        socket.sendText(json.toString())
    }

    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        sink.error(cause)
        socket.sendClose()
    }

    private val log: Logger = LoggerFactory.getLogger(TestWsClient::class.java)

    override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        log.info(frame.toString())
    }

    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        sink.next(JSONObject(text))
    }

    override fun onCloseFrame(websocket: WebSocket, frame: WebSocketFrame) {
        sink.complete()
    }

}