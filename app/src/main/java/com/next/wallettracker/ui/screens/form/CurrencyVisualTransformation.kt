package com.next.wallettracker.ui.screens.form

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.next.wallettracker.ui.utils.formatToCurrency

class CurrencyVisualTransformation : VisualTransformation {


    override fun filter(text: AnnotatedString): TransformedText {

        val original = text.text

        if (original.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formatted = original
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {

                override fun originalToTransformed(offset: Int): Int {
                    val spacesBefore = (offset - 1) / 3
                    return offset + spacesBefore
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val spacesBefore = (offset - 1) / 4
                    return offset - spacesBefore
                }
            }
        )
    }
}