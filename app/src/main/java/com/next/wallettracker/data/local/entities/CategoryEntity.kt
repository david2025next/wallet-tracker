package com.next.wallettracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.next.wallettracker.data.models.TransactionType

@Entity(
    tableName = "Categories"
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val transactionType: TransactionType
)
