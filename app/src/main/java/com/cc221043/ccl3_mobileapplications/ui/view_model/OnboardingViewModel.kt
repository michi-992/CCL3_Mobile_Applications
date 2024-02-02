package com.cc221043.ccl3_mobileapplications.ui.view_model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ViewModel for onboarding
class OnboardingViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    init {
        initializeData()
    }

    // retrieves onboarding completion value through data store
    fun initializeData() {
        viewModelScope.launch {
            _onboardingCompleted.value = dataStore.data.first()[ONBOARDING_COMPLETED_KEY] ?: false
        }
    }

    // sets onboarding completion to true
    fun completeOnboarding() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = true
            }
            _onboardingCompleted.value = true
        }
    }

    // holds the key for onboarding completion
    companion object {
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
    }
}