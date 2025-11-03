package com.example.holaaa.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la UI para la autenticación
data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val userEmail: String? = null
)

// ViewModel para gestionar el estado de autenticación
class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String) {
        // En una app real, aquí iría la lógica para validar con un backend
        _uiState.update {
            it.copy(isLoggedIn = true, userEmail = email)
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(isLoggedIn = false, userEmail = null)
        }
    }

    // Factory para crear el ViewModel sin dependencias
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return AuthViewModel() as T
            }
        }
    }
}