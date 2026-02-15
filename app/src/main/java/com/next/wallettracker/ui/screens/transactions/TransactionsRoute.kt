package com.next.wallettracker.ui.screens.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.next.wallettracker.R
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.domain.models.CategoryWeight
import com.next.wallettracker.ui.components.WalletBottomNavigation
import com.next.wallettracker.ui.utils.toCurrency
import com.next.wallettracker.ui.utils.toDay
import com.next.wallettracker.ui.utils.toFormattedDate
import com.next.wallettracker.ui.utils.toHumanDate
import java.time.LocalDate

private val filters = listOf(
    R.string.tout,
    R.string.revenu,
    R.string.depense
)
private val categoriesColors = listOf(
    Color(0xFF2962FF),
    Color(0xFF2E7D32),
    Color(0xFFFFAB00),
    Color(0xFFFF5252),
    Color(0xFFBDBDBD)
)

@Composable
fun TransactionsRoute(financeViewModel: FinanceViewModel = hiltViewModel()) {

    val uiState by financeViewModel.financeUiState.collectAsStateWithLifecycle()
    FinanceRoute(
        uiState = uiState,
        onSelectedChanged = {
            financeViewModel.updateFilter(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinanceRoute(
    uiState: FinanceUiState,
    onSelectedChanged: (TransactionFilter) -> Unit
) {
    Scaffold(
        bottomBar = {
            WalletBottomNavigation(1)
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = toDay(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.CalendarToday, contentDescription = "Calendar")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { paddingValues ->

        if(uiState.isLoading){

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        else {

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item { FilterSection(selectedFilter = uiState.selectedFilter, onSelectedChanged = onSelectedChanged) }
                item {
                    SpendingSummaryCard(
                        balance = uiState.balance,
                        categories = uiState.categoriesWeight
                    )
                }
                uiState.dailiesTransactions.forEach { dailyTransactions ->
                    stickyHeader(key = dailyTransactions.date) {
                        DateHeader(date = dailyTransactions.date)
                    }
                    items(dailyTransactions.transactions, key = {it.id}) { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterSection(selectedFilter: TransactionFilter, onSelectedChanged: (TransactionFilter) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        TransactionFilter.entries.forEachIndexed { index, filter ->
            val isSelected = selectedFilter == filter
            val res = filters[index % filters.size]
            FilterChip(
                selected = isSelected,
                onClick = { onSelectedChanged(filter) },
                label = { Text(stringResource(res)) },
                shape = RoundedCornerShape(50),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpendingSummaryCard(balance: Double, categories: List<CategoryWeight>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.solde_total),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${balance.toCurrency()} FCFA",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
            ) {
                categories.forEachIndexed { index, cat ->
                    Box(
                        modifier = Modifier
                            .weight(cat.weight)
                            .fillMaxHeight()
                            .background(categoriesColors[index])
                    )
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .background(Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    val color = categoriesColors[index % categoriesColors.size]
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(
                enabled = true,
                onClick = {}
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = transaction.category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = transaction.category.key,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            val amountColor = if (transaction.transactionType == TransactionType.INCOME)
                Color(0xFF2E7D32)
            else
                Color(0xFFD32F2F)

            val amountPrefix =
                if (transaction.transactionType == TransactionType.INCOME) "+$" else "-$"

            Text(
                text = transaction.amount.toCurrency(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Text(
                text = if (transaction.transactionType == TransactionType.INCOME) "Income" else "Expense",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun DateHeader(date: LocalDate) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            horizontal = 16.dp
        )
    ) {
        Text(
            text = date.toHumanDate(),
            style = MaterialTheme.typography.labelLarge
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color.LightGray
        )
    }
}


