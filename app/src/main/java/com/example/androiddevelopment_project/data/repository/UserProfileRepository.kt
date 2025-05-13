package com.example.androiddevelopment_project.data.repository

import com.example.androiddevelopment_project.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateUserProfile(profile: UserProfile)
} 