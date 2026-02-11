package com.next.wallettracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.repository.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    val isLoading: Boolean

    data class NoTransactions(
        override val isLoading: Boolean,
        val totalExpense: Double,
        val totalIncome: Double,
        val balance: Double
    ) : HomeUiState

    data class HasTransactions(
        override val isLoading: Boolean,
        val totalExpense: Double,
        val totalIncome: Double,
        val balance: Double,
        val transactions: List<Transaction>
    ) : HomeUiState
}


private data class HomeViewModelUiState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0,
    val isLoading: Boolean = false,
    val transactions: List<Transaction>? = null
) {

    fun toUiState(): HomeUiState = if (transactions == null) {
        HomeUiState.NoTransactions(
            isLoading = isLoading,
            totalExpense = totalExpense,
            totalIncome = totalIncome,
            balance = balance
        )
    } else HomeUiState.HasTransactions(
        transactions = transactions,
        isLoading = isLoading,
        totalExpense = totalExpense,
        totalIncome = totalIncome,
        balance = balance
    )
}

class HomeViewModel(private val transactionsRepository: TransactionsRepository) : ViewModel() {

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
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            transactionsRepository
                .getBalanceSummaryStream()
                .combine(transactionsRepository.getRecentTransactionsStream()) { balanceSummary, recentTransactions ->
                    viewModelState.update {
                        it.copy(
                            totalIncome = balanceSummary.totalIncome,
                            totalExpense = balanceSummary.totalExpense,
                            transactions = recentTransactions,
                            isLoading = false
                        )
                    }
                }.collect()
        }
    }
}
