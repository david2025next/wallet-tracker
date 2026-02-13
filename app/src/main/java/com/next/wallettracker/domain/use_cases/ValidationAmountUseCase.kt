package com.next.wallettracker.domain.use_cases

import com.next.wallettracker.domain.models.ValidationResult
import javax.inject.Inject

class ValidationAmountUseCase @Inject constructor() {

    operator fun invoke(amount: String): ValidationResult {

       val result =  try {
            val amountNumber = amount.toDouble()
            if (amountNumber < 0) {
                return ValidationResult(
                    success = false,
                    errorMessage = "Veuillez entrer un montant valide"
                )
            }
            if (amountNumber == 0.0) {
                return ValidationResult(
                    success = false,
                    errorMessage = "Veuillez entrer un montant"
                )
            }
            ValidationResult(success = true)
        }catch (e : NumberFormatException){
            if(amount.isBlank()){
                return ValidationResult(
                    success = false,
                    errorMessage = "Veuillez entrer un montant"
                )
            }
           return ValidationResult(
               success = false,
               errorMessage = e.message
           )
        }
        return result

    }
}