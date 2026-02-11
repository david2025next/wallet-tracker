package com.next.wallettracker.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.next.wallettracker.R
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.ui.theme.WallettrackerTheme
import com.next.wallettracker.ui.utils.toDisplayDate

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeRoute(uiState = uiState)
}

@Composable
fun HomeRoute(uiState: HomeUiState) {
    if(uiState.isLoading){
        LoadingContent()
    } else{
        when (uiState) {
            is HomeUiState.HasTransactions -> HasTransactionsScreen(
                balance = uiState.balance,
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                recentTransactions = uiState.transactions
            )

            is HomeUiState.NoTransactions -> EmptyTransactionsScreen(
                image = painterResource(R.drawable.empty_wallet),
                title = R.string.title_empty_home,
                description = R.string.description_empty_home,
                textButton = R.string.text_button_empty_home
            ) { }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {HomeTopBar(R.string.app_name)}
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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
    Scaffold(
        topBar = {HomeTopBar(R.string.app_name)}
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){

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
    }
}

@Composable
private fun HasTransactionsScreen(
    balance: Double,
    totalExpense: Double,
    totalIncome: Double,
    recentTransactions: List<Transaction>,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {HomeTopBar(R.string.app_name)}
    ) { paddingValues ->

        Column(
            modifier = modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
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
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
private fun BalanceCardOverview(balance: Double, totalExpense: Double, totalIncome: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Solde Total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = String.format("%.0f FCFA", balance),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.TrendingUp,
                            contentDescription = "Revenus",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Revenus",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = String.format("+%.0f", totalIncome),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.TrendingDown,
                            contentDescription = "Dépenses",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Dépenses",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = String.format("-%.0f", totalExpense),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun HasTransactionsScreenPreview(){
    WallettrackerTheme {
        Scaffold { paddingValues ->
            HasTransactionsScreen(
                balance = 12000.0,
                totalExpense = 25000.0,
                totalIncome = 45000.0,
                recentTransactions = emptyList(),
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}