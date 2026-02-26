package com.next.wallettracker.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.next.wallettracker.R
import com.next.wallettracker.ui.common.EmptyState

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeRoute(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(uiState: HomeUiState, modifier: Modifier = Modifier) {

    when (uiState) {
        HomeUiState.Loading -> {
            LoadingContent()
        }

        is HomeUiState.Success -> {

            if (uiState.transactions.isNotEmpty()) {
                HasTransactionsScreen(
                    balance = uiState.balance,
                    totalIncome = uiState.totalIncomeMonthly,
                    totalExpense = uiState.totalExpenseMonthly,
                    recentTransactions = uiState.transactions
                )
            } else {
                EmptyState(
                    image = painterResource(R.drawable.empty_wallet),
                    title = R.string.title_empty_home,
                    description = R.string.description_empty_home
                )
            }
        }
    }
}