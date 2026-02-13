package com.next.wallettracker.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class WalletTrackerNavigationActions(navController: NavHostController) {

    val navigateToHome: () -> Unit = {
        navController.navigate(WalletTrackerDestination.HOME.route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToTransactions: () -> Unit = {
        navController.navigate(WalletTrackerDestination.TRANSACTIONS.route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToAnalytics: () -> Unit = {
        navController.navigate(WalletTrackerDestination.ANALYSIS.route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}