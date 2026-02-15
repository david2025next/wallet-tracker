package com.next.wallettracker.domain.use_cases

import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.domain.models.CategoryWeight
import com.next.wallettracker.domain.models.DailyTransactions
import com.next.wallettracker.ui.utils.toDate
import javax.inject.Inject

class CalculateFinanceStatsUseCase @Inject constructor() {

    data class Result(
        val categoryWeights : List<CategoryWeight>,
        val dailyTransactions : List<DailyTransactions>
    )

    operator fun invoke(transactions : List<Transaction>) : Result{

        val expenseTransactions = transactions.filter { it.transactionType == TransactionType.EXPENSE }
        val totalExpense = expenseTransactions.sumOf { it.amount }
        val categoriesWeight = expenseTransactions
            .groupBy { it.category.key }
            .map { (categoryName, txs) ->
                val categoryTotal = txs.sumOf { it.amount }
                CategoryWeight(
                    name = categoryName,
                    weight = if (totalExpense == 0.0) 0f else (categoryTotal / totalExpense).toFloat()
                )
            }
            .sortedByDescending { it.weight }
            .take(5)


        val dailies = transactions
            .groupBy { it.createdAt.toDate() }
            .map { (date, txs) ->
                DailyTransactions(
                    date = date,
                    transactions = txs
                )
            }
            .sortedByDescending { it.date }

        return Result(
            categoryWeights = categoriesWeight,
            dailyTransactions = dailies
        )
    }
}