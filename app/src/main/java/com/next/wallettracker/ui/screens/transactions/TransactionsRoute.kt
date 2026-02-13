package com.next.wallettracker.ui.screens.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.next.wallettracker.ui.components.WalletBottomNavigation

@Composable
fun TransactionsRoute(modifier: Modifier = Modifier) {

    Scaffold(
        bottomBar = { WalletBottomNavigation(1) }
    ) { paddingValues ->
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("transactions screen")
        }
    }
}