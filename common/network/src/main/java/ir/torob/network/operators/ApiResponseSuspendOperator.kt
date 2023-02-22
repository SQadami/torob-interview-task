package ir.torob.network.operators

import ir.torob.network.ApiResponse

/**
 * ApiResponseSuspendOperator operates on an [ApiResponse] which should be handled in the suspension scope.
 * This allows you to handle success and error response instead of the [ir.torob.core.network.suspendOnSuccess],
 * [ir.torob.core.network.suspendOnError], [ir.torob.core.network.suspendOnException] transformers.
 * This operator can be applied globally as a singleton instance, or on each [ApiResponse] one by one.
 */
abstract class ApiResponseSuspendOperator<T> : IOperator {

    /**
     * Operates the [ApiResponse.Success] for handling successful responses if the request succeeds.
     *
     * @param apiResponse The successful response.
     */
    abstract suspend fun onSuccess(apiResponse: ApiResponse.Success<T>)

    /**
     * Operates the [ApiResponse.Failure.Error] for handling error responses if the request failed.
     *
     * @param apiResponse The failed response.
     */
    abstract suspend fun onError(apiResponse: ApiResponse.Failure.Error<T>)

    /**
     * Operates the [ApiResponse.Failure.Exception] for handling exception responses if the request get an exception.
     *
     * @param apiResponse The exception response.
     */
    abstract suspend fun onException(apiResponse: ApiResponse.Failure.Exception<T>)
}