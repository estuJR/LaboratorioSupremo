package uvg.plataformas.labs.presentation.screen2

import uvg.plataformas.labs.data.local.entity.UsuarioEntity

sealed class Screen2UiState {
    object Loading : Screen2UiState()
    data class Error(val message: String) : Screen2UiState()
    data class Success(
        val usuario: UsuarioEntity,
        val isOffline: Boolean,
        val lastUpdate: String
    ) : Screen2UiState()
}