package com.yakovskij.mafiacards.core.errorhandler

import androidx.compose.runtime.mutableStateOf

object ErrorManager {
    val currentError = mutableStateOf<ErrorData?>(null)

    fun showError(error: Throwable, isCritical: Boolean = false) {
        currentError.value = ErrorData(error.message ?: "Unknown error", isCritical)
    }

    fun clear() {
        currentError.value = null
    }
}

data class ErrorData(
    val message: String,
    val isCritical: Boolean
)
