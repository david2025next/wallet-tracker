package com.next.wallettracker.ui

sealed class WalletTrackerDestination(val route : String){

    data object HOME : WalletTrackerDestination("HOME")
    data object TRANSACTIONS : WalletTrackerDestination("TRANSACTIONS")
    data object ANALYSIS : WalletTrackerDestination("ANALYSIS")
    data object FORM : WalletTrackerDestination("FORM")
}