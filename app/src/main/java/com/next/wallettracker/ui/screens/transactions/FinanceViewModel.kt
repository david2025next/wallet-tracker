package com.next.wallettracker.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.data.repository.TransactionsRepository
import com.next.wallettracker.domain.models.CategoryWeight
import com.next.wallettracker.domain.models.DailyTransactions
import com.next.wallettracker.domain.use_cases.CalculateFinanceStatsUseCase
import com.next.wallettracker.ui.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


sealed interface FinanceUiState {

    data object LOADING : FinanceUiState
    data class HasContent(
        val balance: Double,
        val selectedFilter: TransactionFilter,
        val categoriesWeight: List<CategoryWeight>,
        val dailiesTransactions: List<DailyTransactions>
    ) : FinanceUiState

    data class HasEmpty(
        val selectedFilter: TransactionFilter
    ) : FinanceUiState
}

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    private val calculateFinanceStatsUseCase: CalculateFinanceStatsUseCase
) : ViewModel() {

    private var _selectedFilter = MutableStateFlow(TransactionFilter.ALL)

    val financeUiState: StateFlow<FinanceUiState> = combine(
        transactionsRepository.getBalance(),
        transactionsRepository.getAllTransactionsStream(),
        _selectedFilter
    ) { balance, allTransactions, filter ->
        val filteredTransactions = when (filter) {
            TransactionFilter.ALL -> allTransactions
            TransactionFilter.INCOME -> allTransactions.filter { it.transactionType == TransactionType.INCOME }
            TransactionFilter.EXPENSE -> allTransactions.filter { it.transactionType == TransactionType.EXPENSE }
        }

        if (filteredTransactions.isEmpty()) {
            return@combine FinanceUiState.HasEmpty(selectedFilter = _selectedFilter.value)
        } else {
            val balanceForTransactionFilter = if (filter != TransactionFilter.ALL) null else balance
            val stats =
                calculateFinanceStatsUseCase(filteredTransactions, balanceForTransactionFilter)
           return@combine FinanceUiState.HasContent(
                dailiesTransactions = stats.dailyTransactions,
                categoriesWeight = stats.categoryWeights,
                selectedFilter = filter,
                balance = stats.totalBalance
            )
        }

    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            FinanceUiState.LOADING
        )


    fun onRemoveItem(id : Long){
        viewModelScope.launch {
            transactionsRepository.remove(id)
        }
    }
    fun updateFilter(filter: TransactionFilter) {
        _selectedFilter.update { filter }
    }
}


enum class TransactionFilter {
    ALL, INCOME, EXPENSE
}