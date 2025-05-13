package com.example.androiddevelopment_project.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.androiddevelopment_project.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class UserProfileRepositoryImpl(private val context: Context) : UserProfileRepository {
    
    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val _userProfileFlow = MutableStateFlow(getUserProfileFromPrefs())
    
    companion object {
        private const val PREF_NAME = "user_profile_prefs"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_AVATAR_URI = "avatar_uri"
        private const val KEY_RESUME_URL = "resume_url"
        private const val KEY_POSITION = "position"
    }
    
    override fun getUserProfile(): Flow<UserProfile> {
        return _userProfileFlow
    }
    
    override suspend fun updateUserProfile(profile: UserProfile) {
        withContext(Dispatchers.IO) {
            preferences.edit {
                putString(KEY_FULL_NAME, profile.fullName)
                putString(KEY_AVATAR_URI, profile.avatarUri)
                putString(KEY_RESUME_URL, profile.resumeUrl)
                putString(KEY_POSITION, profile.position)
            }
            _userProfileFlow.update { profile }
        }
    }
    
    private fun getUserProfileFromPrefs(): UserProfile {
        return UserProfile(
            fullName = preferences.getString(KEY_FULL_NAME, "") ?: "",
            avatarUri = preferences.getString(KEY_AVATAR_URI, "") ?: "",
            resumeUrl = preferences.getString(KEY_RESUME_URL, "") ?: "",
            position = preferences.getString(KEY_POSITION, "") ?: ""
        )
    }
} 