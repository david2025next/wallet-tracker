package com.next.wallettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.next.wallettracker.data.local.entities.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertAll(categories: List<CategoryEntity>)

    //suspend fun remove(id : Long)
}