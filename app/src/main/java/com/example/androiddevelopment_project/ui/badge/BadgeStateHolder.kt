package com.example.androiddevelopment_project.ui.badge

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class BadgeStateHolder {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()
    
    fun setBadgeVisibility(show: Boolean) {
        _showBadge.value = show
    }
    
    fun updateBadgeState(hasActiveFilters: Boolean) {
        _showBadge.value = hasActiveFilters
    }
    
    fun resetBadgeState() {
        _showBadge.value = false
    }
} 