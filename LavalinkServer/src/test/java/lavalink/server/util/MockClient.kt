package lavalink.server.util

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import org.json.JSONObject
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

class MockClient(uri: String, password: String): WebSocketAdapter() {

    private val socket: WebSocket
    private lateinit var sink: FluxSink<JSONObject>

    init {
         socket = WebSocketFactory().createSocket(uri)
             .addHeader("Authorization", password)
             .addHeader("User-Id", "0")
             .addHeader("Client-name", "MockClient")
             .addListener(this)
    }

    fun connect(): Flux<JSONObject> = Flux.create { sink ->
        this.sink = sink
        socket.connectAsynchronously()
    }

    fun send(json: JSONObject) {
        socket.sendText(json.toString())
    }

    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        sink.error(cause)
        socket.sendClose()
    }

    override fun onTextMessage(websocket: WebSocket, data: ByteArray) {
        sink.next(JSONObject(String(data)))
    }

    override fun onCloseFrame(websocket: WebSocket, frame: WebSocketFrame) {
        sink.complete()
    }

}