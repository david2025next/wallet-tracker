package com.next.wallettracker.data

import com.next.wallettracker.data.local.entities.SummaryEntity
import com.next.wallettracker.data.local.entities.TransactionEntity
import com.next.wallettracker.data.models.BalanceSummary
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.Transaction


fun TransactionEntity.asExternalModel() = Transaction(
    id = id,
    description = description,
    createdAt = createdAt,
    transactionType = type,
    category = Category.getByKey(categoryName),
    amount = amount
)

fun SummaryEntity.asExternalModel() = BalanceSummary(
    totalIncome = totalIncome,
    totalExpense = totalExpense
)