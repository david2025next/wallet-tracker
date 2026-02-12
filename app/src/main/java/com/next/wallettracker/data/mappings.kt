package com.next.wallettracker.data

import com.next.wallettracker.data.local.entities.TransactionEntity
import com.next.wallettracker.data.local.entities.TransactionPeriodTotalsSpentEntity
import com.next.wallettracker.data.models.BalanceSummary
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionPeriodTotalsSpent


fun TransactionEntity.asExternalModel() = Transaction(
    id = id,
    description = description,
    createdAt = createdAt,
    transactionType = type,
    category = Category.getByKey(categoryName),
    amount = amount
)


fun TransactionPeriodTotalsSpentEntity.asExternalModel() = TransactionPeriodTotalsSpent(
    totalsIncome = totalsIncome,
    totalsExpense = totalsExpense
)

fun Transaction.asEntity() = TransactionEntity(
    id = id,
    description = description,
    createdAt = createdAt,
    categoryName = category.key,
    amount = amount,
    type = transactionType
)