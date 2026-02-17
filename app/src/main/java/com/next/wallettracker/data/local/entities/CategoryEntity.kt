package com.next.wallettracker.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.TransactionType

@Entity(
    tableName = "Categories",
    indices = [Index("name", unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val transactionType: TransactionType
)

fun defaultCategories() : List<CategoryEntity>  = Category.entries.map { category ->
    CategoryEntity(
        name = category.key, transactionType = category.type
    )
}