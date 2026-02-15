package com.next.wallettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.next.wallettracker.data.local.entities.MonthlyFinanceStatsEntity
import com.next.wallettracker.data.local.entities.TransactionEntity
import com.next.wallettracker.data.local.entities.TransactionPeriodTotalsSpentEntity
import com.next.wallettracker.data.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {


    @Query("""
    WITH MonthlySums AS (
        -- Étape 1 : On groupe tout par mois
        SELECT 
            strftime('%Y-%m', createdAt / 1000, 'unixepoch') as monthStr,
            SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as totalIncome,
            SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as totalExpense
        FROM transactions
        GROUP BY monthStr
    ),
    PrevMonthCalc AS (
        -- Étape 2 : On regarde dans le rétroviseur (Mois précédent)
        SELECT 
            monthStr,
            totalIncome,
            totalExpense,
            LAG(totalIncome, 1, 0.0) OVER (ORDER BY monthStr) as prevIncome,
            LAG(totalExpense, 1, 0.0) OVER (ORDER BY monthStr) as prevExpense
        FROM MonthlySums
    )
    -- Étape 3 : Calcul final des taux
    SELECT 
        monthStr as month,
        
        totalIncome as incomeAmount,
        CASE 
            WHEN prevIncome = 0 THEN 0.0 
            ELSE ((totalIncome - prevIncome) / prevIncome) * 100 
        END as incomeGrowth,
        
        totalExpense as expenseAmount,
        CASE 
            WHEN prevExpense = 0 THEN 0.0 
            ELSE ((totalExpense - prevExpense) / prevExpense) * 100 
        END as expenseGrowth
        
    FROM PrevMonthCalc
    ORDER BY monthStr DESC
""")
    fun getMonthlyFinanceStatsStream(): Flow<List<MonthlyFinanceStatsEntity>>

    @Query("""
        SELECT *from transactions ORDER BY createdAt DESC 
    """)
    fun getAllTransactionsEntityStream() : Flow<List<TransactionEntity>>


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