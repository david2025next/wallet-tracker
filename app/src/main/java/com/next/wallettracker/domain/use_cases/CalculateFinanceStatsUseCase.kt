package com.next.wallettracker.domain.use_cases

import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.domain.models.CategoryWeight
import com.next.wallettracker.domain.models.DailyTransactions
import com.next.wallettracker.ui.utils.toDate
import javax.inject.Inject

class CalculateFinanceStatsUseCase @Inject constructor() {

    data class Result(
        val totalBalance : Double,
        val categoryWeights : List<CategoryWeight>,
        val dailyTransactions : List<DailyTransactions>
    )

    operator fun invoke(transactions : List<Transaction>, currentBalance : Double ?) : Result{

        val totalBalance = currentBalance ?: transactions.sumOf { it.amount }
        val categoriesWeight = transactions
            .groupBy { it.category.key }
            .map { (categoryName, txs) ->
                val categoryTotal = txs.sumOf { it.amount }
                CategoryWeight(
                    name = categoryName,
                    weight = if (totalBalance == 0.0) 0f else (categoryTotal / totalBalance).toFloat()
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
            totalBalance =totalBalance ,
            categoryWeights = categoriesWeight,
            dailyTransactions = dailies
        )
    }
}