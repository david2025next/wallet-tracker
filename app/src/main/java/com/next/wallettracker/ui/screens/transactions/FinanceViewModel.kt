package com.next.wallettracker.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.data.repository.TransactionsRepository
import com.next.wallettracker.ui.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private var allTransactions: List<Transaction>? = null

    private val _financeUiState = MutableStateFlow(
        FinanceUiState()
    )

    val financeUiState = _financeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                transactionsRepository.getBalance(),
                transactionsRepository.getAllTransactionsStream()
            ) { balance, transactions ->
                allTransactions = transactions
                val dailiesTransactions = transactions.groupTransactionsByDay()
                val categoriesWeight = transactions.groupByCategoryWithPercentage()

                _financeUiState.update {
                    it.copy(
                        balance = balance,
                        dailiesTransactions = dailiesTransactions,
                        categoriesWeight = categoriesWeight
                    )
                }
            }.collect()
        }
    }
}

private fun List<Transaction>.groupByCategoryWithPercentage(): List<CategoryWeight> {
    val totalAmount = this.filter { it.transactionType == TransactionType.EXPENSE }.sumOf { it.amount }
    return this.filter { it.transactionType == TransactionType.EXPENSE }.groupBy { it.category.key }
        .map { (category, transactions) ->
            val categoryTotal = transactions.sumOf { it.amount }
            CategoryWeight(
                name = category,
                weight = if (totalAmount == 0.0) 0f else categoryTotal.toFloat() / totalAmount.toFloat()
            )
        }.sortedByDescending { it.weight }
        .take(5)
}

private fun List<Transaction>.groupTransactionsByDay(): List<DailyTransactions> {
    val grouping = this.groupBy { it.createdAt.toDate() }
    return grouping.map { (key, transactions) ->
        DailyTransactions(
            date = key,
            transactions = transactions
        )
    }
}


data class FinanceUiState(
    val selectedFilter: Int = 0,
    val balance: Double = 0.0,
    val categoriesWeight: List<CategoryWeight> = emptyList(),
    val dailiesTransactions: List<DailyTransactions> = emptyList()
)

data class CategoryWeight(
    val name: String,
    val weight: Float
)

data class DailyTransactions(
    val date: LocalDate,
    val transactions: List<Transaction>
)