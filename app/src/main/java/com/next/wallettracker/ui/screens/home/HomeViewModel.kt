package com.next.wallettracker.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.repository.TransactionsRepository
import com.next.wallettracker.ui.utils.monthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    val isLoading: Boolean

    data class NoTransactions(
        override val isLoading: Boolean,
    ) : HomeUiState

    data class HasTransactions(
        override val isLoading: Boolean,
        val totalExpenseMonthly: Double,
        val totalIncomeMonthly: Double,
        val balance: Double,
        val transactions: List<Transaction>
    ) : HomeUiState
}


private data class HomeViewModelUiState(
    val totalIncomeMonthly: Double = 0.0,
    val totalExpenseMonthly: Double = 0.0,
    val balance: Double = 0.0,
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList()
) {

    fun toUiState(): HomeUiState = if (transactions.isEmpty()) {
        HomeUiState.NoTransactions(
            isLoading = isLoading
        )
    } else HomeUiState.HasTransactions(
        transactions = transactions,
        isLoading = isLoading,
        totalExpenseMonthly = totalExpenseMonthly,
        totalIncomeMonthly = totalIncomeMonthly,
        balance = balance
    )
}


@HiltViewModel
class HomeViewModel @Inject constructor(private val transactionsRepository: TransactionsRepository) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(
        HomeViewModelUiState(
            isLoading = true
        )
    )

    val uiState = viewModelState
        .map(HomeViewModelUiState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        initialize()
    }

    private fun initialize() {
        Log.d("TAG", "initialize: init")
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            combine(
                transactionsRepository.getBalance(),
                transactionsRepository.getRecentTransactionsStream(),
                transactionsRepository.getTotalsSpentByPeriod(monthRange().start, monthRange().end)
            ) { balance, recentsTransactions, totalsSpent ->

                viewModelState.update {
                    it.copy(
                        totalIncomeMonthly = totalsSpent.totalsIncome,
                        totalExpenseMonthly = totalsSpent.totalsExpense,
                        balance = balance,
                        transactions = recentsTransactions,
                        isLoading = false
                    )
                }
            }.collect()
        }
    }
}
