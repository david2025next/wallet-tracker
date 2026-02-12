package com.next.wallettracker.domain.use_cases

import com.next.wallettracker.domain.models.ValidationResult
import javax.inject.Inject

class ValidationDescriptionUseCase @Inject constructor() {

    operator fun invoke(description: String): ValidationResult {

        if (description.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = "Veuillez entrer une description"
            )
        }
        if (description.length < 3) {
            return ValidationResult(
                success = false,
                errorMessage = "Minimum 3 caracteres requis"
            )
        }

        return ValidationResult(success = true)
    }
}