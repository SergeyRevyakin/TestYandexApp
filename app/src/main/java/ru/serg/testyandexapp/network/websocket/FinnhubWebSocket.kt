package ru.serg.testyandexapp.network.websocket

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.retryWhen
import okhttp3.*
import ru.serg.testyandexapp.helper.EndPoints
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class FinnhubWebSocket @Inject constructor(private val client: OkHttpClient) {

    companion object {
        private val TAG = FinnhubWebSocket::class.java.simpleName
    }

    private lateinit var webSocket:WebSocket

    fun disconnect() {
        try {
            webSocket.close(1000, "")
        }catch (e:Exception){}
    }

    fun connect(ticker: String) = callbackFlow<String> {
        val request = Request.Builder().url(EndPoints.FINHUB_WEBSOCKET_URL).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"$ticker\"}")
                Log.d(TAG, "Connected: $response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                offer(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (code != 1000) close(SocketNetworkException("Network Failure"))
                Log.d(TAG, "Closed #$code")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(SocketNetworkException("Network Failure"))
                Log.d(TAG, "Network Failure")
            }
        })

        awaitClose { webSocket.close(1000, "Closed") }
    }
        .retryWhen { cause, attempt ->

            if (attempt > 1) delay(1000 * attempt)
            else if (attempt >= 8) delay(8000)

            cause is SocketNetworkException
        }

    class SocketNetworkException(message: String) : Exception(message)

}