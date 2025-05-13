package com.example.androiddevelopment_project.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment_project.domain.usecase.GetUserProfileUseCase
import com.example.androiddevelopment_project.domain.usecase.UpdateUserProfileUseCase
import com.example.androiddevelopment_project.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class UserProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {
    
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()
    
    private val _editProfileState = MutableStateFlow(UserProfile())
    val editProfileState: StateFlow<UserProfile> = _editProfileState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _favoriteClassTimeError = MutableStateFlow<String?>(null)
    val favoriteClassTimeError: StateFlow<String?> = _favoriteClassTimeError.asStateFlow()
    
    init {
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                _userProfile.value = profile
                _editProfileState.value = profile
            }
        }
    }
    
    fun updateFullName(fullName: String) {
        _editProfileState.update { it.copy(fullName = fullName) }
    }
    
    fun updateAvatarUri(uri: Uri) {
        _editProfileState.update { it.copy(avatarUri = uri.toString()) }
    }
    
    fun updateResumeUrl(url: String) {
        _editProfileState.update { it.copy(resumeUrl = url) }
    }
    
    fun updatePosition(position: String) {
        _editProfileState.update { it.copy(position = position) }
    }
    
    fun updateFavoriteClassTime(time: String) {
        _editProfileState.update { it.copy(favoriteClassTime = time) }
        validateFavoriteClassTime(time)
    }
    
    private fun validateFavoriteClassTime(time: String) {
        _favoriteClassTimeError.value = if (time.isEmpty() || !isValidTimeFormat(time)) {
            "Некорректный формат времени"
        } else {
            null
        }
    }
    
    private fun isValidTimeFormat(time: String): Boolean {
        val pattern = Pattern.compile("^([01]?[0-9]|2[0-3]):([0-5][0-9])$")
        return pattern.matcher(time).matches()
    }
    
    fun isProfileValid(): Boolean {
        validateFavoriteClassTime(_editProfileState.value.favoriteClassTime)
        return _favoriteClassTimeError.value == null
    }
    
    fun saveProfile() {
        if (isProfileValid()) {
            viewModelScope.launch {
                _isLoading.value = true
                updateUserProfileUseCase(_editProfileState.value)
                _isLoading.value = false
            }
        }
    }
    
    fun cancelEditing() {
        _editProfileState.value = _userProfile.value
        _favoriteClassTimeError.value = null
    }
} 