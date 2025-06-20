package com.adri833.orpheus.screens.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.adri833.orpheus.R
import com.adri833.orpheus.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZonedDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun getProfilePicture(): Uri? {
        return authRepository.getProfilePictureUrl()
    }

    fun getUserName(): String? {
        return authRepository.getUserName()
    }

    fun getGreetingMessage(): String {
        val currentHour = ZonedDateTime.now(ZoneId.systemDefault()).hour

        val greeting = when (currentHour) {
            in 6..11 -> context.getString(R.string.good_morning)
            in 12..19 -> context.getString(R.string.good_afternoon)
            else -> context.getString(R.string.good_night)
        }

        return "$greeting, ${getUserName() ?: ""}"
    }
}