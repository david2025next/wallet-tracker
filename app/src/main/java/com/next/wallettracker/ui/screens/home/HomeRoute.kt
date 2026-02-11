package com.next.wallettracker.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ){
            if(uiState.isLoading){
                CircularProgressIndicator()
            } else{
                when(uiState) {
                    is HomeUiState.HasTransactions -> TransactionsList(uiState.transactions)
                    is HomeUiState.NoTransactions -> EmptyTransactionsScreen(
                        image =painterResource(R.drawable.empty_wallet),
                        title = R.string.title_empty_home,
                        description = R.string.description_empty_home,
                        textButton = R.string.text_button_empty_home
                    ) { }
                }
            }
        }
    }
}

@Composable
private fun EmptyTransactionsScreen(
    image: Painter,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes textButton: Int,
    onAddClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text =stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        TextButton(
            onClick = onAddClick
        ) {
            Text(stringResource(textButton))
        }
    }
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