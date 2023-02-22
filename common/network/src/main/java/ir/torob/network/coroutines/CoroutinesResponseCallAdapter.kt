package ir.torob.network.coroutines

import ir.torob.network.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * [CoroutinesResponseCallAdapter] is an coroutines call adapter for creating [ApiResponse]
 * from service method.
 *
 * request API network call asynchronously and returns [ApiResponse].
 */
class CoroutinesResponseCallAdapter constructor(
    private val resultType: Type
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCallDelegate(call)
    }
}