package ir.torob.core.network.mapper

import ir.torob.core.network.ApiResponse
import ir.torob.core.network.message
import ir.torob.core.network.model.ApiErrorResponse

/**
 * A mapper for mapping [ApiResponse.Failure.Error] response as custom [ApiErrorResponse] instance.
 */
object ErrorResponseMapper : ApiErrorModelMapper<ApiErrorResponse> {

    /**
     * maps the [ApiResponse.Failure.Error] to the [ApiErrorResponse] using the mapper.
     *
     * @param apiErrorResponse The [ApiResponse.Failure.Error] error response from the network request.
     * @return A customized [ApiErrorResponse] error response.
     */
    override fun map(apiErrorResponse: ApiResponse.Failure.Error<*>): ApiErrorResponse {
        return ApiErrorResponse(apiErrorResponse.statusCode.code, apiErrorResponse.message())
    }
}