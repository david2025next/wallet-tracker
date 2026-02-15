package com.next.wallettracker.ui.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.next.wallettracker.data.models.Category
import com.next.wallettracker.data.models.Transaction
import com.next.wallettracker.data.models.TransactionType
import com.next.wallettracker.data.repository.TransactionsRepository
import com.next.wallettracker.domain.use_cases.ValidationAmountUseCase
import com.next.wallettracker.domain.use_cases.ValidationDescriptionUseCase
import com.next.wallettracker.ui.utils.toCurrency
import com.next.wallettracker.ui.utils.toDate
import com.next.wallettracker.ui.utils.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class FormTransactionViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    private val validationAmountUseCase: ValidationAmountUseCase,
    private val validationDescriptionUseCase: ValidationDescriptionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _formUiState = MutableStateFlow(FormUiState(isLoading = true))

    val formUiState = _formUiState.asStateFlow()

    init {
        val selectedTransactionId = savedStateHandle.get<Long>("id")
        viewModelScope.launch {
            selectedTransactionId?.let { id ->
                transactionsRepository.getTransactionById(id).collect { transaction ->
                    transaction?.let {
                        _formUiState.update { oldState ->
                            oldState.copy(
                                id = transaction.id,
                                description = it.description,
                                amount = it.amount.toCurrency(),
                                date = it.createdAt.toDate(),
                                category = it.category,
                                transactionType = it.transactionType,
                                isLoading = false
                            )

                        }
                    }
                    _formUiState.update { it.copy(isLoading = false) }
                }
            }
            _formUiState.update { it.copy(isLoading = false) }
        }
    }

    fun uiEvent(event: FormEvent) {
        when (event) {
            is FormEvent.AmountChanged -> _formUiState.update { it.copy(amount = event.amount) }
            is FormEvent.CategoryChanged -> _formUiState.update {
                it.copy(
                    category = Category.getByKey(
                        event.name
                    )
                )
            }

            is FormEvent.DateChanged -> _formUiState.update { it.copy(date = event.date) }
            is FormEvent.DescriptionChanged -> _formUiState.update { it.copy(description = event.description) }
            is FormEvent.TransactionTypeChanged -> _formUiState.update {
                it.copy(
                    transactionType = event.type,
                    categoriesForTransactionType = getCategoriesForTransactionType(event.type),
                    category = if(event.type == TransactionType.INCOME) Category.BUSINESS else Category.FOOD
                )
            }

            FormEvent.Submit -> {
                submit()
            }
        }
    }

    fun reset() {
        _formUiState.update { FormUiState() }
    }

    private fun submit() {

        val resultValidationAmount = validationAmountUseCase(formUiState.value.amount)
        val resultValidationDescription =
            validationDescriptionUseCase(formUiState.value.description)

        val hasError = listOf(
            resultValidationDescription,
            resultValidationAmount
        ).any { !it.success }

        if (hasError) {

            _formUiState.update {
                it.copy(
                    errorAmount = resultValidationAmount.errorMessage,
                    errorDescription = resultValidationDescription.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            transactionsRepository.upsertTransaction(_formUiState.value.toModel())
            _formUiState.update { it.copy(message = "Transaction ajoute avec success") }
        }
    }
}

sealed class FormEvent {

    data class DescriptionChanged(val description: String) : FormEvent()
    data class AmountChanged(val amount: String) : FormEvent()
    data class DateChanged(val date: LocalDate) : FormEvent()
    data class CategoryChanged(val name: String) : FormEvent()
    data class TransactionTypeChanged(val type: TransactionType) : FormEvent()
    data object Submit : FormEvent()
}

data class FormUiState(
    val id: Long = 0,
    val description: String = "",
    val amount: String = "500",
    val date: LocalDate = LocalDate.now(),
    val category: Category = Category.BUSINESS,
    val categoriesForTransactionType: List<Category> = getCategoriesForTransactionType(
        TransactionType.INCOME
    ),
    val transactionType: TransactionType = TransactionType.INCOME,
    val message: String? = null,
    val isLoading: Boolean = false,
    val errorAmount: String? = null,
    val errorDescription: String? = null
) {
    fun toModel(): Transaction = Transaction(
        id = id,
        description = description,
        amount = amount.toDouble(),
        createdAt = date.toMillis(),
        category = category,
        transactionType = transactionType
    )
}

private fun getCategoriesForTransactionType(transactionType: TransactionType): List<Category> =
    Category.entries.filter { it.type == transactionType }