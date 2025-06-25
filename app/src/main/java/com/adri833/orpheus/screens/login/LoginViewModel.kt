package com.adri833.orpheus.screens.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.adri833.orpheus.data.repository.AuthRepository
import androidx.compose.runtime.State
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewModelScope
import com.adri833.orpheus.R
import com.adri833.orpheus.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginState = mutableStateOf<UiState<Unit>>(UiState.Idle)
    val loginState: State<UiState<Unit>> get() = _loginState

    fun loginWithGoogle(context: Context) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                authRepository.loginWithGoogle(context)
                _loginState.value = UiState.Success
            }catch (e: Exception) {
                val message = when (e) {
                    is GetCredentialException -> context.getString(R.string.error_login)
                    else -> e.message ?: context.getString(R.string.error_unknown)
                }
                _loginState.value = UiState.Error(message)
            }
        }
    }

    fun resetState() {
        _loginState.value = UiState.Idle
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.getCurrentUser() != null
    }
}