package com.next.wallettracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.next.wallettracker.ui.components.BottomSheetSceneStrategy
import com.next.wallettracker.ui.components.WalletTrackerBottomBar
import com.next.wallettracker.ui.components.WalletTrackerTopAppBar
import com.next.wallettracker.ui.navigation.Navigator
import com.next.wallettracker.ui.navigation.Route
import com.next.wallettracker.ui.navigation.TOP_LEVEL_BAR
import com.next.wallettracker.ui.navigation.TOP_LEVEL_ROUTES
import com.next.wallettracker.ui.navigation.rememberNavigationState
import com.next.wallettracker.ui.navigation.toEntries
import com.next.wallettracker.ui.analytics.AnalyticsRoute
import com.next.wallettracker.ui.form.FormTransactionRoute
import com.next.wallettracker.ui.home.HomeRoute
import com.next.wallettracker.ui.transactions.TransactionsRoute
import com.next.wallettracker.ui.theme.WallettrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletTrackerApp(modifier: Modifier = Modifier) {

    WallettrackerTheme(darkTheme = true) {

        val navigationState = rememberNavigationState(
            startRoute = Route.HOME,
            topLevelRoutes = TOP_LEVEL_ROUTES.keys
        )
        val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
        val destination = TOP_LEVEL_BAR[navigationState.topLevelRoute] ?: error("Error")
        val navigator = remember { Navigator(navigationState) }

        Scaffold(
            modifier = modifier,
            topBar = {
                WalletTrackerTopAppBar(
                    title = destination.title,
                    navigationIcon = destination.navigationIcon,
                    actionIcon = destination.actionIcon,
                    navigationIconContentDescription = destination.navigationIconContentDescription,
                    actionIconContentDescription = destination.actionIconContentDescription
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .offset(
                            y = 60.dp
                        ),
                    onClick = {
                        navigator.navigate(
                            Route.FORM(
                                transactionId = null,
                                transactionType = null
                            )
                        )
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Add, null
                    )
                }
            },
            bottomBar = {
                WalletTrackerBottomBar(
                    selectedKey = navigationState.topLevelRoute,
                    onSelectedKey = {
                        navigator.navigate(it)
                    }
                )
            }
        ) { paddingValues ->
            NavDisplay(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                sceneStrategy = bottomSheetStrategy,
                entries = navigationState.toEntries(
                    entryProvider {
                        HomeEntry()
                        TransactionsEntry(navigator)
                        //AnalyticsEntry()
                        FormEntry()
                    }
                ),
                onBack = navigator::goBack
            )
        }
    }
}

@Composable
private fun EntryProviderScope<NavKey>.AnalyticsEntry() {
    entry<Route.STATS> {
        AnalyticsRoute()
    }
}

@Composable
private fun EntryProviderScope<NavKey>.TransactionsEntry(navigator: Navigator) {
    entry<Route.TRANSACTIONS> {
        TransactionsRoute(
            onUpdateItem = { navigator.navigate(Route.FORM(it, transactionType = null)) },
            onAddClick = { navigator.navigate(Route.FORM(null, it)) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryProviderScope<NavKey>.FormEntry() {
    entry<Route.FORM>(
        metadata = BottomSheetSceneStrategy.bottomSheet()
    ) {
        FormTransactionRoute(
            transactionId = it.transactionId,
            transactionType = it.transactionType
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryProviderScope<NavKey>.HomeEntry() {
    entry<Route.HOME> {
        HomeRoute()
    }

}
