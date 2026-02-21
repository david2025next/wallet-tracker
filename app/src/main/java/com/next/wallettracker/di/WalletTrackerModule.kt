package com.next.wallettracker.di

import android.content.Context
import androidx.room.Room
import com.next.wallettracker.data.local.DatabaseCallback
import com.next.wallettracker.data.local.WalletAppDataBase
import com.next.wallettracker.data.local.dao.TransactionDao
import com.next.wallettracker.data.repository.TransactionsRepository
import com.next.wallettracker.data.repository.impl.TransactionsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsTransactionRepository(impl: TransactionsRepositoryImpl): TransactionsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesWalletDatabase(
        @ApplicationContext context: Context,
        callback: DatabaseCallback
    ): WalletAppDataBase = Room
        .databaseBuilder(
            context,
            WalletAppDataBase::class.java,
            "walletTrackerDatabase"
        )
        .addCallback(callback)
        .build()

    @Singleton
    @Provides
    fun providesTransactionDao(walletAppDataBase: WalletAppDataBase): TransactionDao =
        walletAppDataBase.transactionDao
}