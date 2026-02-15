package com.next.wallettracker.domain.models

import com.next.wallettracker.data.models.Transaction
import java.time.LocalDate

data class DailyTransactions(
    val date: LocalDate,
    val transactions: List<Transaction>
)