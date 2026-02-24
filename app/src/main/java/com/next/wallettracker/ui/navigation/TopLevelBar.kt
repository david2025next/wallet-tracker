package com.next.wallettracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.next.wallettracker.ui.utils.toDay


data class TopLevelBarItem(
    val title : String,
    val navigationIcon : ImageVector?  =null,
    val navigationIconContentDescription : String? = null,
    val actionIcon : ImageVector? = null,
    val actionIconContentDescription : String ? = null
)

val TOP_LEVEL_BAR = mapOf(
    Route.HOME to TopLevelBarItem(
        title = "Wallet Tracker",
    ),
    Route.TRANSACTIONS to TopLevelBarItem(
        title = toDay(),
        navigationIcon = Icons.Outlined.CalendarToday,
        actionIcon = Icons.Outlined.Search
    ),
    Route.STATS to TopLevelBarItem(
        title = "Analytics"
    ),
)