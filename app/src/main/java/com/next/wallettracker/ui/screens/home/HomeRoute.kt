package com.next.wallettracker.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.next.wallettracker.R
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.ui.utils.toDisplayDate

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeRoute(uiState = uiState)
}

@Composable
fun HomeRoute(uiState: HomeUiState) {

    Scaffold(
        topBar = { HomeTopBar(R.string.app_name) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                when (uiState) {
                    is HomeUiState.HasTransactions -> TransactionsList(uiState.transactions)
                    is HomeUiState.NoTransactions -> EmptyTransactionsScreen(
                        image = painterResource(R.drawable.empty_wallet),
                        title = R.string.title_empty_home,
                        description = R.string.description_empty_home,
                        textButton = R.string.text_button_empty_home
                    ) { }
                }
            }
        }
    }
}

@Composable
private fun EmptyTransactionsScreen(
    image: Painter,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes textButton: Int,
    onAddClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        TextButton(
            onClick = onAddClick
        ) {
            Text(stringResource(textButton))
        }
    }
}

@Composable
private fun HasTransactionsScreen(
    balance: Double,
    totalExpense: Double,
    totalIncome: Double,
    recentTransactions: List<Transaction>
) {
    Column {
        BalanceCardOverview(balance, totalExpense, totalIncome)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Transactions Recentes",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        TransactionsList(transactions = recentTransactions)
    }

}

@Composable
private fun TransactionsList(transactions: List<Transaction>) {
    transactions.forEach { transaction ->
        ListItem(
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
            headlineContent = {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingContent = {

                CustomIcon(
                    icon = transaction.category.icon,
                    size = 48.dp,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    iconSize = 24.dp
                )
            },
            trailingContent = {
                Text(
                    text = "${if (transaction.transactionType == TransactionType.EXPENSE) "-" else "+"} ${transaction.amount}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.transactionType == TransactionType.EXPENSE) MaterialTheme.colorScheme.error else Color(
                        0xff2e7d32
                    )
                )
            },
            supportingContent = {
                Row {
                    Text(
                        text = transaction.category.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = " • ${transaction.createdAt.toDisplayDate()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@Composable
private fun CustomIcon(
    icon: ImageVector,
    size: Dp,
    backgroundColor: Color,
    tint: Color,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(@StringRes title: Int) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(title)
            )
        }
    )
}

@Composable
private fun BalanceCardOverview(balance: Double, totalExpense: Double, totalIncome: Double) {
    /*
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.totalbalance),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${balance.toCurrency()} FCFA",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { percent },
                modifier = Modifier.fillMaxWidth(),
                color = if (percent > 0.5f) MaterialTheme.colorScheme.error else ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }
    }
     */
}