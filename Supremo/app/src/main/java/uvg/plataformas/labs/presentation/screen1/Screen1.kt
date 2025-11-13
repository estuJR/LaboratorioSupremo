@file:OptIn(ExperimentalMaterial3Api::class)

package uvg.plataformas.labs.presentation.screen1


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uvg.plataformas.labs.data.remote.model.Asset
import uvg.plataformas.labs.presentation.components.ErrorScreen
import uvg.plataformas.labs.presentation.components.LoadingScreen
import uvg.plataformas.labs.presentation.components.OfflineBanner
import java.util.Locale

@Composable
fun Screen1Route(
    viewModel: Screen1ViewModel,
    onNavigateToDetail: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                title = { Text("CryptoTracker", color = Color.White) },
                actions = {
                    IconButton(onClick = { viewModel.loadAssets() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        }
    ) { padding ->

        when (uiState) {

            Screen1UiState.Loading -> {
                LoadingScreen(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                )
            }

            is Screen1UiState.Error -> {
                val err = uiState as Screen1UiState.Error
                ErrorScreen(
                    message = err.message,
                    onRetry = { viewModel.loadAssets() },
                    modifier = Modifier.padding(padding)
                )
            }

            is Screen1UiState.Success -> {
                val s = uiState as Screen1UiState.Success

                Screen1Content(
                    assets = s.assets,
                    isOffline = s.isOfflineMode,
                    lastUpdate = s.lastUpdate,
                    onClick = onNavigateToDetail,
                    onOfflineClick = { viewModel.saveOfflineData() },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun Screen1Content(
    assets: List<Asset>,
    isOffline: Boolean,
    lastUpdate: String?,
    onClick: (String) -> Unit,
    onOfflineClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {

        OfflineBanner(isOffline = isOffline, lastUpdate = lastUpdate)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onOfflineClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF333333),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Offline")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(assets) { item ->
                AssetCard(
                    asset = item,
                    onClick = { onClick(item.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun AssetCard(asset: Asset, onClick: () -> Unit) {

    val cambio = (asset.changePercent24Hr ?: "0").toDoubleOrNull() ?: 0.0
    val colorCambio = if (cambio >= 0) Color(0xFF4CAF50) else Color(0xFFFF5252)

    Surface(
        color = Color(0xFF1E1E1E),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(asset.name, color = Color.White)
                Text(asset.symbol, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {

                Text(
                    "$${String.format(Locale.US, "%.2f", asset.priceUsd.toDouble())}",
                    color = Color.White
                )

                Text(
                    "${String.format(Locale.US, "%.2f", cambio)}%",
                    color = colorCambio
                )
            }
        }
    }
}