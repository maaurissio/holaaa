package com.example.holaaa.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login(): Boolean {
        // Lógica de autenticación simulada
        if (uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty()) {
            // Aquí iría la llamada al backend/Firebase
            // Por ejemplo: if (email == "admin@huerto.cl" && password == "1234")
            return true
        }
        return false
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)