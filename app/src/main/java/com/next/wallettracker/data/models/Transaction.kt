package com.next.wallettracker.data.models

data class Transaction(
    val id : Long = 0,
    val description : String,
    val amount : Double,
    val category: Category,
    val createdAt : Long,
    val transactionType: TransactionType
)
