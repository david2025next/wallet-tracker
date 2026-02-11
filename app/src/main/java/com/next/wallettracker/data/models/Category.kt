package com.next.wallettracker.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector


enum class Category(val key : String, val icon : ImageVector, val type: TransactionType){

    // INCOME
    SALARY("Salaire", Icons.Filled.Payments, TransactionType.INCOME),
    BUSINESS("Business", Icons.Filled.BusinessCenter, TransactionType.INCOME),
    SAVINGS("Epargne", Icons.Filled.Savings, TransactionType.INCOME),

    // EXPENSE
    FOOD("Alimentation", Icons.Filled.Restaurant, TransactionType.EXPENSE),
    TRANSPORT("Transport", Icons.Filled.DirectionsCar, TransactionType.EXPENSE),
    HEALTH("Sante", Icons.Filled.MedicalServices, TransactionType.EXPENSE),
    INTERNET("Internet", Icons.Filled.Wifi, TransactionType.EXPENSE);

    companion object {
        fun getByKey(key : String) : Category = entries.find { it.key==key }!!
    }
}


enum class TransactionType{
    INCOME,
    EXPENSE
}
