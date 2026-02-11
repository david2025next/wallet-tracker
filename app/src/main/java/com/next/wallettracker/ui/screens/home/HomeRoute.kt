package com.next.wallettracker.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.next.wallettracker.R
import com.next.wallettracker.data.models.Transaction

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(uiState = uiState)
}

@Composable
fun HomeRoute(uiState: HomeUiState) {

    Scaffold(
        topBar = { HomeTopBar(R.string.app_name) }
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                BalanceCardOverview(uiState.balance, uiState.totalExpense, uiState.totalIncome)
                when (uiState) {
                    is HomeUiState.HasTransactions -> TransactionsList(uiState.transactions)
                    is HomeUiState.NoTransactions -> EmptyView()
                }
            }
        }
    }
}

@Composable
private fun EmptyView() {
    Text(
        text = "EmptyView"
    )
}

@Composable
fun TransactionsList(recentTransactions: List<Transaction>) {
    Text(
        text = "Transactions"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(@StringRes title: Int) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(title)
            )
        }
    )
}

@Composable
private fun BalanceCardOverview(balance: Double, totalExpense: Double, totalIncome: Double) {

}