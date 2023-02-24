package ir.torob.data.product

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.torob.data.product.detail.ProductDetailDataSource
import ir.torob.data.product.detail.ProductDetailDataSourceImpl
import ir.torob.data.product.simular.SimilarProductDataSource
import ir.torob.data.product.simular.SimilarProductDataSourceImpl
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideSimilarProductDataSource(
        apiService: ProductApiService
    ): SimilarProductDataSource = SimilarProductDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideProductDetailDataSource(
        apiService: ProductApiService
    ): ProductDetailDataSource = ProductDetailDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService =
        retrofit.create(ProductApiService::class.java)
}