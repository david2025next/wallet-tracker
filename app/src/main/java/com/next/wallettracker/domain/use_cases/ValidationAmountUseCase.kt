package com.next.wallettracker.domain.use_cases

import com.next.wallettracker.domain.models.ValidationResult
import javax.inject.Inject

class ValidationAmountUseCase @Inject constructor() {

    operator fun invoke(amount: Double): ValidationResult {

        if (amount < 0) {
            return ValidationResult(
                success = false,
                errorMessage = "Veuillez entrer un montant valide"
            )
        }
        if (amount == 0.0) {
            return ValidationResult(
                success = false,
                errorMessage = "Veuillez entrer un montant"
            )
        }

        return ValidationResult(success = true)
    }
}