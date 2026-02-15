package com.next.wallettracker.data.local.entities

data class MonthlyFinanceStatsEntity(
    val month: String,
    val incomeAmount: Double,
    val incomeGrowth: Float,
    val expenseAmount: Double,
    val expenseGrowth: Float
)