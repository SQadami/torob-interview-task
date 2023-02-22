package ir.torob.sample.di;

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.torob.core.Const.API_URL
import ir.torob.core.Const.TIMEOUT
import ir.torob.network.coroutines.CoroutinesResponseCallAdapterFactory
import ir.torob.sample.BuildConfig
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()

    /**
     * Provides an instance of [OkHttpClient] with the authentication injectors pre-setup, so that
     * authentication is already handled.
     */
    @Provides
    @Reusable
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor?,
        loggingEventListener: LoggingEventListener.Factory?,
    ): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .apply {
                httpLoggingInterceptor?.let { addNetworkInterceptor(it) }
                loggingEventListener?.let { eventListenerFactory(it) }
            }
            .cache(Cache(File(context.cacheDir, "api_cache"), 50L * 1024 * 1024))
            // Adjust the Connection pool to account for historical use of 3 separate clients
            // but reduce the keepAlive to 2 minutes to avoid keeping radio open.
            .connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
            .dispatcher(
                Dispatcher().apply {
                    // Allow for high number of concurrent image fetches on same host.
                    maxRequestsPerHost = 15
                }
            )
            .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? =
        if (!BuildConfig.DEBUG) null
        else HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideHttpEventListener(): LoggingEventListener.Factory? =
        if (!BuildConfig.DEBUG) null
        else LoggingEventListener.Factory()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
}