package com.next.wallettracker.ui.navigation

import android.util.Log
import androidx.navigation3.runtime.NavKey

class Navigator(val navigationState: NavigationState) {


    fun navigate(route: NavKey) {

        if (route in navigationState.backStacks.keys) {
            navigationState.topLevelRoute = route
            Log.d("TAG", "navigate: $route")
        } else {
            navigationState.backStacks[navigationState.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentBack = navigationState.backStacks[navigationState.topLevelRoute] ?: error("back stack error")
        val currentRoute = currentBack.last()
        if(currentRoute == navigationState.topLevelRoute){
            navigationState.topLevelRoute = navigationState.startRoute
        } else{
            currentBack.removeLastOrNull()
        }
    }
}