package ru.serg.testyandexapp.network

import android.util.Log
import retrofit2.Response
import ru.serg.testyandexapp.helper.Resource

abstract class BaseDataSource {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {

        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.toString().isNotBlank()) {
                    return Resource.success(body)
                } else return error("${response.code()} ${response.message()}")
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e("remoteDataSource", message)
        return Resource.error("Network call has failed for a following reason: $message")
    }

}