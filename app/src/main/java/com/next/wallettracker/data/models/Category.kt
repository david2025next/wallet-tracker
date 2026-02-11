package com.next.wallettracker.data.models

data class Category(
    val id : Long = 0,
    val name : String,
    val transactionType : TransactionType
)

enum class TransactionType{
    INCOME,
    EXPENSE
}
