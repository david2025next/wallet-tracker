package com.next.wallettracker.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.next.wallettracker.ui.common.BottomNavShape
import com.next.wallettracker.ui.navigation.TOP_LEVEL_ROUTES

@Composable
fun WalletTrackerBottomBar(
    selectedKey: NavKey,
    onSelectedKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {

    BottomAppBar(
        modifier = modifier
            .clip(
                BottomNavShape(
                    cornerRadius = with(LocalDensity.current) { 0.dp.toPx() },
                    dockRadius = with(LocalDensity.current) { 48.dp.toPx() },
                )
            )
        ,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        TOP_LEVEL_ROUTES.forEach { (topLevelDestination, bottomItem) ->
            NavigationBarItem(
                selected = selectedKey == topLevelDestination,
                onClick = {
                    onSelectedKey(topLevelDestination)
                },
                icon = {
                    Icon(
                        imageVector = if (selectedKey == topLevelDestination) bottomItem.selectedIcon else bottomItem.unselectedIcon,
                        stringResource(bottomItem.title)
                    )
                },
                label = {
                    Text(stringResource(bottomItem.title))
                }
            )
        }
    }
}