package com.next.wallettracker.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.next.wallettracker.R
import com.next.wallettracker.ui.WalletTrackerDestination
import com.next.wallettracker.ui.WalletTrackerNavigationUtils


@Composable
fun WalletBottomNavigation(selectedItemIndex: Int) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        itemsBottomNavigation.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick ={ WalletTrackerNavigationUtils.navigate(item.screen)},
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(item.title)
                    )
                },
                label = { Text(text = stringResource(item.title)) }
            )
        }
    }
}

data class BottomNavigationItem(
    @StringRes val title : Int,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    val screen : WalletTrackerDestination
)

val itemsBottomNavigation = listOf(
    BottomNavigationItem(
        title = R.string.accueil,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        screen = WalletTrackerDestination.HOME
    ),
    BottomNavigationItem(
        title = R.string.transactions,
        selectedIcon = Icons.Filled.SwapHoriz,
        unselectedIcon = Icons.Outlined.SwapHoriz,
        screen = WalletTrackerDestination.TRANSACTIONS
    ),
    BottomNavigationItem(
        title = R.string.analyse,
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Analytics,
        screen = WalletTrackerDestination.ANALYSIS
    )
)