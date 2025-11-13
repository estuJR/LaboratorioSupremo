package uvg.plataformas.labs.presentation.screen1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uvg.plataformas.labs.data.local.dao.UsuarioDao
import uvg.plataformas.labs.data.local.entity.UsuarioEntity
import uvg.plataformas.labs.data.remote.ApiService
import uvg.plataformas.labs.data.remote.model.Asset
import java.text.SimpleDateFormat
import java.util.*

sealed class Screen1UiState {
    object Loading : Screen1UiState()

    data class Success(
        val assets: List<Asset>,
        val isOfflineMode: Boolean,
        val lastUpdate: String?
    ) : Screen1UiState()

    data class Error(val message: String) : Screen1UiState()
}

class Screen1ViewModel(
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<Screen1UiState>(Screen1UiState.Loading)
    val uiState: StateFlow<Screen1UiState> = _uiState

    init {
        loadAssets()
    }

    fun loadAssets() {
        viewModelScope.launch {
            Log.d("Screen1VM", "Loading assets...")
            _uiState.value = Screen1UiState.Loading

            try {
                val response = apiService.getAssets()
                Log.d("Screen1VM", "Response: ${response.isSuccess}")

                if (response.isSuccess) {
                    val data = response.getOrNull()
                    if (data != null && data.data.isNotEmpty()) {
                        Log.d("Screen1VM", "Got ${data.data.size} assets")
                        _uiState.value = Screen1UiState.Success(
                            assets = data.data,
                            isOfflineMode = false,
                            lastUpdate = getCurrentTimestamp()
                        )
                    } else {
                        Log.e("Screen1VM", "Data is null or empty")
                        loadFromCache()
                    }
                } else {
                    Log.e("Screen1VM", "Response failed")
                    loadFromCache()
                }
            } catch (e: Exception) {
                Log.e("Screen1VM", "Exception: ${e.message}", e)
                loadFromCache()
            }
        }
    }

    private suspend fun loadFromCache() {
        val local = usuarioDao.getAllUsuarios()

        if (local.isNotEmpty()) {
            Log.d("Screen1VM", "Loaded ${local.size} from cache")
            _uiState.value = Screen1UiState.Success(
                assets = local.map { it.toAsset() },
                isOfflineMode = true,
                lastUpdate = local.firstOrNull()?.fechaGuardado
            )
        } else {
            Log.e("Screen1VM", "No cached data")
            _uiState.value = Screen1UiState.Error(
                "No hay conexión y no hay datos guardados"
            )
        }
    }

    fun saveOfflineData() {
        viewModelScope.launch {
            val state = uiState.value
            if (state is Screen1UiState.Success) {
                val fecha = getCurrentTimestamp()

                usuarioDao.deleteAll()

                state.assets.forEach {
                    usuarioDao.insert(it.toEntity(fecha))
                }

                _uiState.value = Screen1UiState.Success(
                    assets = state.assets,
                    isOfflineMode = true,
                    lastUpdate = fecha
                )
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}

private fun Asset.toEntity(timestamp: String): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombre = name,
        simbolo = symbol,
        precioUsd = priceUsd,
        changePercent24Hr = changePercent24Hr ?: "0",
        supply = supply ?: "",
        maxSupply = maxSupply ?: "",
        marketCapUsd = marketCapUsd ?: "",
        fechaGuardado = timestamp
    )
}

private fun UsuarioEntity.toAsset(): Asset {
    return Asset(
        id = id,
        symbol = simbolo,
        name = nombre,
        priceUsd = precioUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = if (maxSupply.isNotEmpty()) maxSupply else null,
        marketCapUsd = marketCapUsd,
        rank = "0",
        volumeUsd24Hr = null,
        vwap24Hr = null
    )
}