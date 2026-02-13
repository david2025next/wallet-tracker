package com.next.wallettracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.next.wallettracker.ui.screens.analytics.AnalyticsRoute
import com.next.wallettracker.ui.screens.home.HomeRoute
import com.next.wallettracker.ui.screens.transactions.TransactionsRoute

@Composable
fun WalletTrackerNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = WalletTrackerDestination.HOME.route
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(
            route = WalletTrackerDestination.HOME.route
        ){
            HomeRoute()
        }

        composable(
            route = WalletTrackerDestination.TRANSACTIONS.route
        ){
            TransactionsRoute()
        }

        composable(
            route = WalletTrackerDestination.ANALYSIS.route
        ){
            AnalyticsRoute()
        }

    }
}