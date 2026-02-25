package com.next.wallettracker.ui.screens.transactions

import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.next.wallettracker.R
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.domain.models.CategoryWeight
import com.next.wallettracker.ui.screens.home.CustomIcon
import com.next.wallettracker.ui.screens.home.EmptyState
import com.next.wallettracker.ui.screens.transactions.TransactionFilter.ALL
import com.next.wallettracker.ui.theme.WallettrackerTheme
import com.next.wallettracker.ui.utils.toCurrency
import com.next.wallettracker.ui.utils.toHumanDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

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
fun TransactionsRoute(
    financeViewModel: FinanceViewModel = hiltViewModel(),
    onUpdateItem: (Long) -> Unit,
    onAddClick: (TransactionType?) -> Unit
) {

    val uiState by financeViewModel.financeUiState.collectAsStateWithLifecycle()
    FinanceRoute(
        uiState = uiState,
        onSelectedChanged = {
            financeViewModel.updateFilter(it)
        },
        onUpdateItem = onUpdateItem,
        onAddClick = onAddClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinanceRoute(
    modifier: Modifier = Modifier,
    uiState: FinanceUiState,
    onSelectedChanged: (TransactionFilter) -> Unit,
    onUpdateItem: (Long) -> Unit,
    onAddClick: (TransactionType?) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(ALL) }
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        FilterSection(
            selectedFilter = selectedFilter,
            onSelectedChanged = { transactionFilter ->
                selectedFilter = transactionFilter
                onSelectedChanged(transactionFilter)
            }
        )
        when (uiState) {
            is FinanceUiState.HasContent -> {
                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        bottom = 80.dp,
                        start = 8.dp,
                        top = 0.dp,
                        end = 8.dp,
                    )
                ) {
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
                        items(dailyTransactions.transactions, key = { it.id }) { transaction ->
                            TransactionItem(transaction, onUpdateItem = onUpdateItem)
                        }
                    }
                }
            }

            is FinanceUiState.HasEmpty -> {
                val (title, description, image) = when (selectedFilter) {
                    ALL -> {
                        Triple(
                            first = R.string.title_empty_all,
                            second = R.string.description_empty_all,
                            third = painterResource(R.drawable.empty_wallet)
                        )
                    }

                    TransactionFilter.INCOME -> {
                        Triple(
                            first = R.string.title_empty_income,
                            second = R.string.description_empty_income,
                            third = painterResource(R.drawable.empty_income)
                        )
                    }

                    TransactionFilter.EXPENSE -> {
                        Triple(
                            first = R.string.title_empty_expense,
                            second = R.string.description_empty_expense,
                            third = painterResource(R.drawable.empty_expense)
                        )
                    }
                }
                EmptyState(
                    image = image,
                    description = description,
                    title = title,
                    onTextAction = R.string.text_action,
                    onAction = {
                        val result = when (selectedFilter) {
                            ALL -> null
                            TransactionFilter.INCOME -> TransactionType.INCOME
                            TransactionFilter.EXPENSE -> TransactionType.EXPENSE
                        }
                        onAddClick(result)
                    }
                )
            }

            FinanceUiState.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: TransactionFilter,
    onSelectedChanged: (TransactionFilter) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
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
            .fillMaxWidth(),
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

enum class DragAction {
    Center,
    EndRevealed
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    onUpdateItem: (id: Long) -> Unit
) {


    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val actionWidth = with(density) { 70.dp.toPx() }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragAction.Center,
            positionalThreshold = { d -> d * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
            anchors = DraggableAnchors {
                DragAction.Center at 0f
                DragAction.EndRevealed at -actionWidth
            }
        )
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterEnd)
        ) {

            FilledIconButton(
                modifier = Modifier.size(50.dp),
                onClick = { onUpdateItem(transaction.id) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    null
                )
            }
        }

        ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .offset {
                    IntOffset(
                        dragState.requireOffset().roundToInt(), 0
                    )
                }
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Horizontal
                )
                .clickable(
                    onClick = {
                        scope.launch {
                            dragState.animateTo(DragAction.Center)
                        }
                    },
                    enabled = true
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
            supportingContent = {
                Text(
                    text = transaction.category.key,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                Column {
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
        )
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


