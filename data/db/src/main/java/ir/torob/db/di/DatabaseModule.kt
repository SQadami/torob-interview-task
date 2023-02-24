package ir.torob.db.di

import android.app.Application
import android.os.Debug
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.torob.db.DatabaseTransactionRunner
import ir.torob.db.ProductDatabase
import ir.torob.db.dao.LastRequestDao
import ir.torob.db.dao.ProductDao
import ir.torob.db.dao.SimilarDao
import ir.torob.db.room.ProductRoomDatabase
import ir.torob.db.room.RoomTransactionRunner
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProductRoomDatabase(
        application: Application,
    ): ProductRoomDatabase {
        val builder =
            Room.databaseBuilder(
                application,
                ProductRoomDatabase::class.java,
                "product.db"
            )
                .fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Provides
    fun provideDatabaseTransactionRunner(
        bind: ProductRoomDatabase
    ): DatabaseTransactionRunner = RoomTransactionRunner(bind)

    @Provides
    fun provideProductDatabase(bind: ProductRoomDatabase): ProductDatabase = bind

    @Provides
    fun provideProductDao(db: ProductDatabase): ProductDao = db.productDao()

    @Provides
    fun provideSimilarDao(db: ProductDatabase): SimilarDao = db.similarDao()

    @Provides
    fun provideLastRequestDao(db: ProductDatabase): LastRequestDao = db.lastRequestDao()
}