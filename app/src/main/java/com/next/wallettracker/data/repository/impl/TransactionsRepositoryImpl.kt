package com.next.wallettracker.data.repository.impl

import com.next.wallettracker.data.asExternalModel
import com.next.wallettracker.data.local.dao.TransactionDao
import com.next.wallettracker.data.local.entities.SummaryEntity
import com.next.wallettracker.data.local.entities.TransactionEntity
import com.next.wallettracker.data.models.BalanceSummary
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionsRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionsRepository {


    override fun getRecentTransactionsStream(count: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsEntitiesStream(count)
            .map { it.map(TransactionEntity::asExternalModel) }

    override fun getBalanceSummaryStream(): Flow<BalanceSummary> =
        transactionDao.getSummaryEntitiesStream()
            .map(SummaryEntity::asExternalModel)
}