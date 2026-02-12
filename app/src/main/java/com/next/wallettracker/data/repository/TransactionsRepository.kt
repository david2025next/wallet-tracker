package com.next.wallettracker.data.repository

import com.next.wallettracker.data.models.BalanceSummary
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionPeriodTotalsSpent
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getRecentTransactionsStream(count : Int = 5) : Flow<List<Transaction>>
    fun getBalance(): Flow<Double>
    fun getTotalsSpentByPeriod(start : Long, end : Long) : Flow<TransactionPeriodTotalsSpent>
    suspend fun upsertTransaction(transaction: Transaction)
    fun getTransactionById(id : Long) : Flow<Transaction?>
}