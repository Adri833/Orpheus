package com.adri833.orpheus.data.repository

import android.content.Context
import android.net.Uri
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.adri833.orpheus.constants.OrpheusConstants.GOOGLE_ID
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    // Login with Google
    suspend fun loginWithGoogle(context: Context) {
        val credentialManager = CredentialManager.create(context)

        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(GOOGLE_ID)
            .setNonce("nonce")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

        auth.signInWithCredential(firebaseCredential).await()
    }

    // Profile Picture
    fun getProfilePictureUrl(): Uri? {
        return auth.currentUser?.photoUrl
    }

    // Username
    fun getUserName(): String? {
        return auth.currentUser?.displayName?.trim()?.split(" ")?.firstOrNull()
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

}