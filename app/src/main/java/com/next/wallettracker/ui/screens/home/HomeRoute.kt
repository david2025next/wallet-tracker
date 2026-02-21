package com.next.wallettracker.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.next.wallettracker.R
import com.next.wallettracker.ui.WalletTrackerDestination
import com.next.wallettracker.ui.WalletTrackerNavigationUtils

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeRoute(uiState = uiState)
}

@Composable
fun HomeRoute(uiState: HomeUiState) {

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
                ) { WalletTrackerNavigationUtils.navigate(WalletTrackerDestination.FORM) }
            } else {
                EmptyTransactionsScreen(
                    image = painterResource(R.drawable.empty_wallet),
                    title = R.string.title_empty_home,
                    description = R.string.description_empty_home,
                    textButton = R.string.text_button_empty_home
                ) { WalletTrackerNavigationUtils.navigate(WalletTrackerDestination.FORM) }
            }
        }
    }
}