package com.adri833.orpheus.screens.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.adri833.orpheus.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getProfilePicture(): Uri? {
        return authRepository.getProfilePictureUrl()
    }
}