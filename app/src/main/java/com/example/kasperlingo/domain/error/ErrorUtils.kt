package com.example.kasperlingo.domain.error

import android.content.Context
import com.example.kasperlingo.R
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

// обрабатывает разные типы ошибок
@Singleton
class ErrorUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    // превращает техническую ошибку в понятное сообщение
    fun handleError(throwable: Throwable): AppError {
        return when (throwable) {
            is UnknownHostException -> AppError.NetworkError(context.getString(R.string.error_no_internet))
            is IOException -> AppError.NetworkError(context.getString(R.string.error_network))
            is HttpException -> {
                when (throwable.code()) {
                    404 -> AppError.NotFoundError(context.getString(R.string.error_translation_not_found))
                    500 -> AppError.ServerError(context.getString(R.string.error_server))
                    else -> AppError.NetworkError(context.getString(R.string.error_network))
                }
            }
            else -> AppError.UnknownError(throwable.message ?: context.getString(R.string.error_unknown))
        }
    }
}

sealed class AppError(val message: String) {
    data class NetworkError(val errorMessage: String) : AppError(errorMessage)
    data class NotFoundError(val errorMessage: String) : AppError(errorMessage)
    data class ServerError(val errorMessage: String) : AppError(errorMessage)
    data class UnknownError(val errorMessage: String) : AppError(errorMessage)
}
