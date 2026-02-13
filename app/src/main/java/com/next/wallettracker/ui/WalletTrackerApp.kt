package com.next.wallettracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.next.wallettracker.ui.theme.WallettrackerTheme

@Composable
fun WalletTrackerApp(modifier: Modifier = Modifier) {

    WallettrackerTheme {
        val navController = rememberNavController()
        val navigationActions = WalletTrackerNavigationActions(navController = navController)
        WalletTrackerNavigationUtils.walletTrackerNavigationActions = navigationActions
        WalletTrackerNavGraph(
            navController = navController,
            modifier = modifier
        )
    }
}