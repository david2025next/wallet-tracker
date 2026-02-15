package com.next.wallettracker.data.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import com.next.wallettracker.R


enum class Category(val key : String, val icon : ImageVector, val type: TransactionType){

    // INCOME
    SALARY("Salaire", Icons.Filled.Payments, TransactionType.INCOME),
    BUSINESS("Business", Icons.Filled.BusinessCenter, TransactionType.INCOME),
    SAVINGS("Epargne", Icons.Filled.Savings, TransactionType.INCOME),
    SALE("Vente", Icons.Filled.Sell, TransactionType.INCOME),
    LOAN("Pret", Icons.Filled.AccountBalance, TransactionType.INCOME),

    // EXPENSE
    FOOD("Alimentation", Icons.Filled.Restaurant, TransactionType.EXPENSE),
    TRANSPORT("Transport", Icons.Filled.DirectionsCar, TransactionType.EXPENSE),
    HEALTH("Sante", Icons.Filled.MedicalServices, TransactionType.EXPENSE),
    REFUND("Remboursement", Icons.Filled.Replay, TransactionType.EXPENSE),
    HOUSE("Maison", Icons.Filled.Home, TransactionType.EXPENSE),
    INTERNET("Internet", Icons.Filled.Wifi, TransactionType.EXPENSE);

    companion object {
        fun getByKey(key : String) : Category = entries.find { it.key==key }!!
    }
}


enum class TransactionType(@StringRes val displayName : Int){
    INCOME(R.string.revenu),
    EXPENSE(R.string.depense)
}
