package com.next.wallettracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.next.wallettracker.data.local.dao.TransactionDao
import com.next.wallettracker.data.local.entities.CategoryEntity
import com.next.wallettracker.data.local.entities.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class],
    version = 1
)
abstract class WalletAppDataBase : RoomDatabase() {

    abstract val transactionDao : TransactionDao
}