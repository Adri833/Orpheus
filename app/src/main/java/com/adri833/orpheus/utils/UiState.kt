package com.adri833.orpheus.utils

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object Success : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
}