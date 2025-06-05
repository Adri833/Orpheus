package com.adri833.orpheus.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adri833.orpheus.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun loginWithGoogle(context: Context, scope: CoroutineScope) {
        authRepository.loginWithGoogle(context, scope)
    }
}