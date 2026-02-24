package com.next.wallettracker.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.next.wallettracker.R
import kotlinx.serialization.Serializable

data class BottomNavItem(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


sealed interface Route : NavKey {

    @Serializable
    data object HOME : Route

    @Serializable
    data object TRANSACTIONS : Route

    @Serializable
    data object STATS : Route

    @Serializable
    data object FORM : Route
}


val TOP_LEVEL_ROUTES = mapOf(
    Route.HOME to BottomNavItem(
        title = R.string.accueil,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    Route.TRANSACTIONS to BottomNavItem(
        R.string.transactions,
        selectedIcon = Icons.Filled.SwapHoriz,
        unselectedIcon = Icons.Outlined.SwapHoriz,
    ),
    Route.STATS to BottomNavItem(
        title = R.string.analyse,
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Analytics,
    )
)