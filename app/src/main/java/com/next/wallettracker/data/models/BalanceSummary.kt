package com.next.wallettracker.data.models

data class BalanceSummary(
    val totalIncome : Double,
    val totalExpense : Double
){

    val balance : Double
        get() = totalIncome-totalExpense
}
