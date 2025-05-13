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
    
    // Методы для экрана редактирования профиля
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
    
    // Сохранение изменений профиля
    fun saveProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            updateUserProfileUseCase(_editProfileState.value)
            _isLoading.value = false
        }
    }
    
    // Отмена изменений профиля
    fun cancelEditing() {
        _editProfileState.value = _userProfile.value
    }
} 