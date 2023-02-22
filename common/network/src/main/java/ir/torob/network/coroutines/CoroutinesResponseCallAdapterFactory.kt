package ir.torob.network.coroutines

import ir.torob.network.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * [CoroutinesResponseCallAdapterFactory] is an coroutines call adapter factory for creating [ApiResponse].
 *
 * Adding this class to [Retrofit] allows you to return on [ApiResponse] from service method.
 *
 * ```
 * @GET("Feeds.json")
 * suspend fun fetchFeedsList(): ApiResponse<List<Feed>>
 * ```
 */
class CoroutinesResponseCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {

        @JvmStatic
        fun create(): CoroutinesResponseCallAdapterFactory =
            CoroutinesResponseCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CoroutinesResponseCallAdapter? = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                ApiResponse::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    CoroutinesResponseCallAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}