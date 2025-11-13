package uvg.plataformas.labs.presentation.screen2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2Route(
    assetId: String,
    viewModel: Screen2ViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(key1 = assetId) {
        viewModel.load(assetId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Screen2UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Screen2UiState.Error -> {
            Text(
                text = (uiState as Screen2UiState.Error).message,
                modifier = Modifier.padding(20.dp)
            )
        }

        is Screen2UiState.Success -> {
            val usuario = (uiState as Screen2UiState.Success).usuario

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detalle") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Regresar"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(20.dp)
                ) {
                    Text("Nombre: ${usuario.nombre}")
                    Text("Símbolo: ${usuario.simbolo}")
                    Text("Precio: ${usuario.precioUsd}")
                    Text("Cambio 24h: ${usuario.changePercent24Hr}")
                    Text("Supply: ${usuario.supply}")
                    Text("Max Supply: ${usuario.maxSupply}")
                    Text("Market Cap: ${usuario.marketCapUsd}")
                    Text("Actualizado: ${usuario.fechaGuardado}")
                }
            }
        }
    }
}