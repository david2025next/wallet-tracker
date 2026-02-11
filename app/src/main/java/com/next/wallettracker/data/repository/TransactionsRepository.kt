package com.next.wallettracker.data.repository

import com.next.wallettracker.data.models.BalanceSummary
import com.next.wallettracker.data.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getRecentTransactionsStream(count : Int = 5) : Flow<List<Transaction>>
    fun getBalanceSummaryStream() : Flow<BalanceSummary>
}