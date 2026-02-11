package com.next.wallettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.next.wallettracker.data.local.entities.SummaryEntity
import com.next.wallettracker.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query(
        """
        SELECT * FROM transactions ORDER BY createdAt DESC LIMIT :limit
    """
    )
    fun getTransactionsEntitiesStream(limit: Int): Flow<List<TransactionEntity>>

    @Query(
        """
            SELECT
                SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) AS totalExpense,
                SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) AS totalIncome
          FROM Transactions
        """
    )
    fun getSummaryEntitiesStream(): Flow<SummaryEntity>
}