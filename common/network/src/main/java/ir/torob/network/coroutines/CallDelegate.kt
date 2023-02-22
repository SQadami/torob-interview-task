package ir.torob.network.coroutines

import ir.torob.network.ApiResponseInitializer
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * CallDelegate is a delegate [Call] proxy for handling and transforming one to another generic types
 * between the two different types of [Call] requests.
 */
internal abstract class CallDelegate<IN, OUT>(
    protected val proxy: Call<IN>
) : Call<OUT> {

    override fun execute(): Response<OUT> = throw NotImplementedError()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = ApiResponseInitializer.timeout ?: proxy.timeout()

    final override fun enqueue(callback: Callback<OUT>) = enqueueImpl(callback)
    final override fun clone(): Call<OUT> = cloneImpl()

    abstract fun enqueueImpl(callback: Callback<OUT>)
    abstract fun cloneImpl(): Call<OUT>
}