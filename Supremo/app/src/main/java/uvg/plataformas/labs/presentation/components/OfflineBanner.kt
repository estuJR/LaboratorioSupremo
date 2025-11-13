package uvg.plataformas.labs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OfflineBanner(
    isOffline: Boolean,
    lastUpdate: String?
) {

    if (isOffline && lastUpdate != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D2D2D))
                .padding(12.dp)
        ) {
            Text(
                text = "Viendo data del $lastUpdate",
                color = Color.White
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4A3FC8))
                .padding(12.dp)
        ) {
            Text(
                text = "Viendo data más reciente",
                color = Color.White
            )
        }
    }
}