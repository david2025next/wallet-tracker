package com.next.wallettracker.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.next.wallettracker.data.local.entities.defaultCategories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(private val database: Provider<WalletAppDataBase>) :
    RoomDatabase.Callback() {

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        CoroutineScope(Dispatchers.IO).launch {
            database.get().categoryDao.insertAll(defaultCategories())
        }
    }
}