package com.next.wallettracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.repository.TransactionsRepository
import com.next.wallettracker.ui.utils.monthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val totalIncomeMonthly: Double,
        val totalExpenseMonthly: Double,
        val balance: Double,
        val transactions: List<Transaction>
    ) : HomeUiState
}


@HiltViewModel
class HomeViewModel @Inject constructor(private val transactionsRepository: TransactionsRepository) :
    ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        transactionsRepository.getBalance(),
        transactionsRepository.getRecentTransactionsStream(),
        transactionsRepository.getTotalsSpentByPeriod(monthRange().start, monthRange().end)
    ) { balance, recentsTransactions, totalsSpent ->

        HomeUiState.Success(
            totalIncomeMonthly = totalsSpent.totalsIncome,
            totalExpenseMonthly = totalsSpent.totalsExpense,
            balance = balance,
            transactions = recentsTransactions
        )
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeUiState.Loading
        )

}
