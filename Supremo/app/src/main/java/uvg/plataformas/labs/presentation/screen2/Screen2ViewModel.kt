package uvg.plataformas.labs.presentation.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uvg.plataformas.labs.data.local.dao.UsuarioDao
import uvg.plataformas.labs.data.local.entity.UsuarioEntity
import uvg.plataformas.labs.data.remote.ApiService

class Screen2ViewModel(
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<Screen2UiState>(Screen2UiState.Loading)
    val uiState: StateFlow<Screen2UiState> = _uiState

    fun load(assetId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getAssetDetail(assetId).getOrThrow()
                val item = response.data

                val entidad = UsuarioEntity(
                    id = item.id,
                    nombre = item.name,
                    simbolo = item.symbol,
                    precioUsd = item.priceUsd,
                    changePercent24Hr = item.changePercent24Hr ?: "-",
                    supply = item.supply ?: "-",
                    maxSupply = item.maxSupply ?: "-",
                    marketCapUsd = item.marketCapUsd ?: "-",
                    fechaGuardado = "Datos recientes"
                )

                usuarioDao.insert(entidad)

                _uiState.value = Screen2UiState.Success(
                    usuario = entidad,
                    isOffline = false,
                    lastUpdate = "Datos recientes"
                )
            } catch (e: Exception) {
                loadCached(assetId)
            }
        }
    }

    private suspend fun loadCached(id: String) {
        val cached = usuarioDao.getUsuarioById(id)

        if (cached != null) {
            _uiState.value = Screen2UiState.Success(
                usuario = cached,
                isOffline = true,
                lastUpdate = cached.fechaGuardado
            )
        } else {
            _uiState.value = Screen2UiState.Error("No se encontraron datos guardados")
        }
    }
}