package com.next.wallettracker.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithCategory(

    @Embedded val transaction : TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity
)
