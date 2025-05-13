package com.example.androiddevelopment_project.domain.usecase

import com.example.androiddevelopment_project.data.repository.UserProfileRepository
import com.example.androiddevelopment_project.model.UserProfile
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val repository: UserProfileRepository) {
    operator fun invoke(): Flow<UserProfile> {
        return repository.getUserProfile()
    }
}

class UpdateUserProfileUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.updateUserProfile(profile)
    }
} 