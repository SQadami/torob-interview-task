@file:Suppress("unused")

package ir.torob.network

import ir.torob.network.exception.NoContentException
import ir.torob.network.operators.ApiResponseOperator
import ir.torob.network.operators.ApiResponseSuspendOperator
import ir.torob.network.operators.IOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * ApiResponse is an interface for constructing standard responses from the retrofit call.
 */
sealed class ApiResponse<out T> {

    /**
     * API Success response class from OkHttp request call.
     * The [data] is a nullable generic type. (A response without data)
     *
     * @param response A response from OkHttp request call.
     *
     * @property statusCode [StatusCode] is Hypertext Transfer Protocol (HTTP) response status codes.
     * @property headers The header fields of a single HTTP message.
     * @property raw The raw response from the HTTP client.
     * @property data The de-serialized response body of a successful data.
     */
    data class Success<T>(val response: Response<T>) : ApiResponse<T>() {
        val statusCode: StatusCode = getStatusCodeFromResponse(response)
        val headers: Headers = response.headers()
        val raw: okhttp3.Response = response.raw()
        val data: T by lazy { response.body() ?: throw NoContentException(statusCode.code) }
        override fun toString(): String = "[ApiResponse.Success](data=$data)"
    }

    /**
     * API Failure response class from OkHttp request call.
     * There are two subtypes: [ApiResponse.Failure.Error] and [ApiResponse.Failure.Exception].
     */
    sealed class Failure<T> {
        /**
         * API response error case.
         * API communication conventions do not match or applications need to handle errors.
         * e.g., internal server error.
         *
         * @param response A response from OkHttp request call.
         *
         * @property statusCode [StatusCode] is Hypertext Transfer Protocol (HTTP) response status codes.
         * @property headers The header fields of a single HTTP message.
         * @property raw The raw response from the HTTP client.
         * @property errorBody The [ResponseBody] can be consumed only once.
         */
        data class Error<T>(val response: Response<T>) : ApiResponse<T>() {
            val statusCode: StatusCode = getStatusCodeFromResponse(response)
            val headers: Headers = response.headers()
            val raw: okhttp3.Response = response.raw()
            val errorBody: ResponseBody? = response.errorBody()
            override fun toString(): String =
                "[ApiResponse.Failure.Error-$statusCode](errorResponse=$response)"
        }

        /**
         * API request Exception case.
         * An unexpected exception occurs while creating requests or processing an response in the client side.
         * e.g., network connection error, timeout.
         *
         * @param exception An throwable exception.
         *
         * @property message The localized message from the exception.
         */
        data class Exception<T>(val exception: Throwable) : ApiResponse<T>() {
            val message: String? = exception.localizedMessage
            override fun toString(): String = "[ApiResponse.Failure.Exception](message=$message)"
        }
    }

    companion object {
        /**
         * [Failure] factory function. Only receives [Throwable] as an argument.
         *
         * @param ex A throwable.
         *
         * @return A [ApiResponse.Failure.Exception] based on the throwable.
         */
        fun <T> error(ex: Throwable): Failure.Exception<T> =
            Failure.Exception<T>(ex).apply { operate() }

        /**
         * ApiResponse Factory.
         *
         * @param successCodeRange A success code range for determining the response is successful or failure.
         * @param [f] Create [ApiResponse] from [retrofit2.Response] returning from the block.
         * If [retrofit2.Response] has no errors, it creates [ApiResponse.Success].
         * If [retrofit2.Response] has errors, it creates [ApiResponse.Failure.Error].
         * If [retrofit2.Response] has occurred exceptions, it creates [ApiResponse.Failure.Exception].
         *
         * @return An [ApiResponse] model which holds information about the response.
         */
        @JvmSynthetic
        inline fun <T> of(
            successCodeRange: IntRange = ApiResponseInitializer.successCodeRange,
            crossinline f: () -> Response<T>
        ): ApiResponse<T> = try {
            val response = f()
            if (response.raw().code in successCodeRange) {
                Success(response)
            } else {
                Failure.Error(response)
            }
        } catch (ex: Exception) {
            Failure.Exception(ex)
        }.operate()

        /**
         * Operates if there is a global [IOperator]
         * which operates on [ApiResponse]s globally on each response and returns the target [ApiResponse].
         *
         * @return [ApiResponse] A target [ApiResponse].
         */
        @PublishedApi
        @Suppress("UNCHECKED_CAST")
        internal fun <T> ApiResponse<T>.operate(): ApiResponse<T> = apply {
            val globalOperator = ApiResponseInitializer.operator ?: return@apply
            if (globalOperator is ApiResponseOperator<*>) {
                operator(globalOperator as ApiResponseOperator<T>)
            } else if (globalOperator is ApiResponseSuspendOperator<*>) {
                val context = ApiResponseInitializer.operatorContext
                val supervisorJob = SupervisorJob(context[Job])
                val scope = CoroutineScope(context + supervisorJob)
                scope.launch {
                    suspendOperator(globalOperator as ApiResponseSuspendOperator<T>)
                }
            }
        }

        /**
         * Returns a status code from the [Response].
         *
         * @param response A network callback response.
         *
         * @return A [StatusCode] from the network callback response.
         */
        fun <T> getStatusCodeFromResponse(response: Response<T>): StatusCode {
            return StatusCode.values().find { it.code == response.code() }
                ?: StatusCode.Unknown
        }
    }
}