package ir.torob.network.model

/**
 * A customized api error response.
 *
 * @param code A network response code.
 * @param message A network error message.
 */
data class ApiErrorResponse(
    val code: Int,
    val message: String?
)