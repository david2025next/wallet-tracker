package com.next.wallettracker.ui


object WalletTrackerNavigationUtils{

    var walletTrackerNavigationActions : WalletTrackerNavigationActions? =null

    fun navigate(walletTrackerDestination: WalletTrackerDestination){
        when(walletTrackerDestination) {
            WalletTrackerDestination.ANALYSIS -> walletTrackerNavigationActions?.navigateToAnalytics()
            WalletTrackerDestination.HOME -> walletTrackerNavigationActions?.navigateToHome()
            WalletTrackerDestination.TRANSACTIONS -> walletTrackerNavigationActions?.navigateToTransactions()
            WalletTrackerDestination.FORM -> walletTrackerNavigationActions?.navigateToForm()
        }
    }
}