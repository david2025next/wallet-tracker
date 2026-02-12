package com.next.wallettracker.data.repository.impl


import com.next.wallettracker.data.asEntity
import com.next.wallettracker.data.asExternalModel
import com.next.wallettracker.data.local.dao.TransactionDao
import com.next.wallettracker.data.local.entities.TransactionEntity
import com.next.wallettracker.data.models.BalanceSummary
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionPeriodTotalsSpent
import com.next.wallettracker.data.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionsRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionsRepository {


    override fun getRecentTransactionsStream(count: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsEntitiesStream(count)
            .map { it.map(TransactionEntity::asExternalModel) }

    override fun getBalance(): Flow<Double> = transactionDao.getBalance()

    override fun getTotalsSpentByPeriod(
        start: Long,
        end: Long
    ): Flow<TransactionPeriodTotalsSpent>  = transactionDao.getTotalsSpentPeriodTotalsStream(start, end)
        .map{it.asExternalModel()}


    override suspend fun upsertTransaction(transaction: Transaction) {
        transactionDao.upsert(transaction.asEntity())
    }

    override fun getTransactionById(id: Long): Flow<Transaction?> =
        transactionDao.getTransactionEntityByIdStream(id).map { it?.asExternalModel() }

}