package com.example.androiddevelopment_project.ui.badge

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Класс для хранения состояния бейджа на экране настроек.
 * Используется для отображения индикатора на вкладке настроек, 
 * когда фильтры отличаются от дефолтных значений.
 */
class BadgeStateHolder {
    // Состояние, показывающее должен ли бейдж отображаться
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()
    
    // Метод для установки состояния бейджа
    fun setBadgeVisibility(show: Boolean) {
        _showBadge.value = show
    }
    
    // Метод для обновления состояния бейджа на основе наличия активных фильтров
    fun updateBadgeState(hasActiveFilters: Boolean) {
        _showBadge.value = hasActiveFilters
    }
    
    // Метод для сброса состояния бейджа
    fun resetBadgeState() {
        _showBadge.value = false
    }
} 