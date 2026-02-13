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

fun defaultCategories() : List<CategoryEntity> = listOf(

    CategoryEntity(name = Category.SALARY.key, transactionType = Category.SALARY.type),
    CategoryEntity(name = Category.BUSINESS.key, transactionType = Category.BUSINESS.type),
    CategoryEntity(name = Category.SAVINGS.key, transactionType = Category.SAVINGS.type),
    CategoryEntity(name = Category.FOOD.key, transactionType = Category.FOOD.type),
    CategoryEntity(name = Category.TRANSPORT.key, transactionType = Category.TRANSPORT.type),
    CategoryEntity(name = Category.HEALTH.key, transactionType = Category.HEALTH.type),
    CategoryEntity(name = Category.INTERNET.key, transactionType = Category.INTERNET.type)
)