package com.next.wallettracker.ui.screens.transactions

import androidx.lifecycle.ViewModel
import com.next.wallettracker.data.models.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor() : ViewModel() {

    private val _financeUiState = MutableStateFlow(
        FinanceUiState()
    )

    val financeUiState = _financeUiState.asStateFlow()
}


data class FinanceUiState(
    val selectedFilter : Int = 0,
    val balance : Double = 0.0,
    val categoriesUiState: List<CategoryUiState> = emptyList(),
    val dailiesTransactions : List<DailyTransactions> = emptyList()
)

data class CategoryUiState(
    val name : String,
    val weight : Float
)

data class DailyTransactions(
    val date: LocalDate,
    val transactions: List<Transaction>
)