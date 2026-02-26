package com.next.wallettracker.ui.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrency() : String{
    val format = NumberFormat
        .getInstance()
        .format(this)
    return "$format Fcfa"
}