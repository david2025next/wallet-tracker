package com.next.wallettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.next.wallettracker.data.local.entities.TransactionEntity
import com.next.wallettracker.data.local.entities.TransactionPeriodTotalsSpentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsert(transactionEntity: TransactionEntity)

    @Query("""
        SELECT 
            SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) 
                - 
            SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END)
        FROM transactions
    """)
    fun getBalance() : Flow<Double>

    @Query(
        """
        SELECT
         SUM(CASE WHEN (type = 'INCOME' and (createdAt BETWEEN :start and :end)) THEN amount ELSE 0 END) as totalsIncome,
         SUM(CASE WHEN (type = 'EXPENSE' and (createdAt BETWEEN :start and :end)) THEN amount ELSE 0 END) as totalsExpense
        FROM transactions
    """
    )
    fun getTotalsSpentPeriodTotalsStream(
        start: Long,
        end: Long
    ): Flow<TransactionPeriodTotalsSpentEntity>

    @Query(
        """
        SELECT * FROM transactions ORDER BY createdAt DESC LIMIT :limit
    """
    )
    fun getTransactionsEntitiesStream(limit: Int): Flow<List<TransactionEntity>>


    @Query("""
        SELECT * FROM transactions WHERE id =:id
    """)
    fun getTransactionEntityByIdStream(id: Long): Flow<TransactionEntity?>
}