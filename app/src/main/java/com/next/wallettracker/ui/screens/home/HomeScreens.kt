package com.next.wallettracker.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.SouthEast
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.next.wallettracker.R
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.ui.components.WalletBottomNavigation
import com.next.wallettracker.ui.theme.WallettrackerTheme
import com.next.wallettracker.ui.utils.humanReadableDateMonth
import com.next.wallettracker.ui.utils.toCurrency

@Composable
fun HasTransactionsScreen(
    balance: Double,
    totalExpense: Double,
    totalIncome: Double,
    recentTransactions: List<Transaction>,
    modifier: Modifier = Modifier,
    addClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            WalletBottomNavigation(selectedItemIndex = 0)
        },
        topBar = { HomeTopBar(R.string.app_name) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = addClick,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add transaction"
                )
            }
        }
    ) { paddingValues ->

        Dashboard(modifier, paddingValues, balance, totalExpense, totalIncome, recentTransactions)
    }
}


@Composable
private fun Dashboard(
    modifier: Modifier,
    paddingValues: PaddingValues,
    balance: Double,
    totalExpense: Double,
    totalIncome: Double,
    recentTransactions: List<Transaction>
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        BalanceCardOverview(balance, totalExpense, totalIncome)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.transactions_recentes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = stringResource(R.string.voir_tout),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null
                )
            }
        }
        TransactionsList(transactions = recentTransactions)
    }
}

@Composable
fun EmptyTransactionsScreen(
    image: Painter,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes textButton: Int,
    onAddClick: () -> Unit
) {
    Scaffold(
        topBar = { HomeTopBar(R.string.app_name) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
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
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { HomeTopBar(R.string.app_name) }
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
private fun TransactionsList(transactions: List<Transaction>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction)
        }
    }

}

@Composable
private fun TransactionItem(transaction: Transaction) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                enabled = true,
                onClick = {}
            ),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        headlineContent = {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyMedium,
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
                text = "${if (transaction.transactionType == TransactionType.EXPENSE) "-" else "+"} ${transaction.amount.toCurrency()}",
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
                    text = transaction.category.key,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = " • ${transaction.createdAt.humanReadableDateMonth()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
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
                text = stringResource(R.string.solde_total),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${balance.toCurrency()} FCFA",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = " Revenu du mois",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = totalIncome.toCurrency(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowOutward,
                                contentDescription = null,
                                tint = Color(0xff2e7d32),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "5.2%",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xff2e7d32),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = " Dépense du mois",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = " ${totalExpense.toCurrency()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Icon(
                                imageVector = Icons.Filled.SouthEast,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "2.6%",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Surface(
//                        shape = CircleShape,
//                        color = MaterialTheme.colorScheme.tertiaryContainer,
//                        modifier = Modifier.size(40.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Rounded.TrendingUp,
//                            contentDescription = stringResource(R.string.revenu),
//                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
//                            modifier = Modifier.padding(10.dp)
//                        )
//                    }
//                    Column {
//                        Text(
//                            text = stringResource(R.string.revenu),
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
//                        )
//                        Text(
//                            text =totalIncome.toCurrency(),
//                            style = MaterialTheme.typography.titleSmall.copy(
//                                fontWeight = FontWeight.SemiBold
//                            ),
//                            color = MaterialTheme.colorScheme.tertiary
//                        )
//                    }
//                }

//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Surface(
//                        shape = CircleShape,
//                        color = MaterialTheme.colorScheme.errorContainer,
//                        modifier = Modifier.size(40.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Rounded.TrendingDown,
//                            contentDescription = "Dépenses",
//                            tint = MaterialTheme.colorScheme.onErrorContainer,
//                            modifier = Modifier.padding(10.dp)
//                        )
//                    }
//                    Column {
//                        Text(
//                            text = stringResource(R.string.depense),
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
//                        )
//                        Text(
//                            text = totalExpense.toCurrency(),
//                            style = MaterialTheme.typography.titleSmall.copy(
//                                fontWeight = FontWeight.SemiBold
//                            ),
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
            }
        }
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


@Preview
@Composable
private fun TransactionItemPreview() {

    val transaction = Transaction(
        id = 1,
        description = "Salaire Janvier",
        amount = 250000.0,
        category = Category.SALARY,
        createdAt = System.currentTimeMillis(),
        transactionType = TransactionType.INCOME
    )
    TransactionItem(
        transaction
    )
}

@Preview
@Composable
private fun HasTransactionsScreenPreview() {
    val transactions = listOf(

        Transaction(
            id = 1,
            description = "Salaire Janvier",
            amount = 250000.0,
            category = Category.SALARY,
            createdAt = System.currentTimeMillis(),
            transactionType = TransactionType.INCOME
        ),

        Transaction(
            id = 2,
            description = "Vente application étudiant",
            amount = 50000.0,
            category = Category.BUSINESS,
            createdAt = System.currentTimeMillis(),
            transactionType = TransactionType.INCOME
        ),

        Transaction(
            id = 3,
            description = "Déjeuner restaurant",
            amount = 4500.0,
            category = Category.FOOD,
            createdAt = System.currentTimeMillis(),
            transactionType = TransactionType.EXPENSE
        ),

        Transaction(
            id = 4,
            description = "Taxi pour l'école",
            amount = 3000.0,
            category = Category.TRANSPORT,
            createdAt = System.currentTimeMillis(),
            transactionType = TransactionType.EXPENSE
        ),

        Transaction(
            id = 5,
            description = "Abonnement Internet",
            amount = 20000.0,
            category = Category.INTERNET,
            createdAt = System.currentTimeMillis(),
            transactionType = TransactionType.EXPENSE
        )
    )

    WallettrackerTheme {
        Scaffold { paddingValues ->
            HasTransactionsScreen(
                balance = 120000000.0,
                totalExpense = 2500000.0,
                totalIncome = 4500000.0,
                recentTransactions = transactions,
                modifier = Modifier.padding(paddingValues)
            ) {}
        }
    }
}