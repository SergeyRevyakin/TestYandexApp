package ru.serg.testyandexapp.network.websocket
//
//import kotlinx.coroutines.channels.Channel
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.WebSocket
//import ru.serg.testyandexapp.data.response.TradesResponse
//import java.util.concurrent.TimeUnit
//
//class WebSocketProvider {
//
//    private var _webSocket: WebSocket? = null
//
//    private val socketOkHttpClient = OkHttpClient.Builder()
//        .readTimeout(30, TimeUnit.SECONDS)
//        .connectTimeout(39, TimeUnit.SECONDS)
//        .hostnameVerifier { _, _ -> true }
//        .build()
//
////    val okHttpClient = OkHttpClient.Builder()
//    //        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
////        .addInterceptor(AlphaVantageAuthInterceptor(EndPoints.ALPHA_VANTAGE_API_KEY))
////        .connectTimeout(15, TimeUnit.SECONDS)
////        .readTimeout(15, TimeUnit.SECONDS)
////        .build()
////    val request = Request.Builder()
////        .url("wss://ws.finnhub.io?token=c0vufrn48v6t383ls200")
////        .build()
////    val webSocket = okHttpClient.newWebSocket(request, FinnhubWebSocketListener())
//    private var _webSocketListener: FinnhubWebSocketListener? = null
//
//    fun startSocket(): Channel<TradesResponse> =
//        with(FinnhubWebSocketListener()) {
//            startSocket(this)
//            this@with.socketEventChannel
//        }
//
//    fun startSocket(webSocketListener: FinnhubWebSocketListener) {
//        _webSocketListener = webSocketListener
//        _webSocket = socketOkHttpClient.newWebSocket(
//            Request.Builder().url("wss://ws.finnhub.io?token=c0vufrn48v6t383ls200").build(),
//            webSocketListener
//        )
//        socketOkHttpClient.dispatcher.executorService.shutdown()
//    }
//
//    fun stopSocket() {
//        try {
//            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
//            _webSocket = null
//            _webSocketListener?.socketEventChannel?.close()
//            _webSocketListener = null
//        } catch (ex: Exception) {
//        }
//    }
//
//    companion object {
//        const val NORMAL_CLOSURE_STATUS = 1000
//    }
//
//}
//
//}