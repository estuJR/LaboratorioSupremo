package uvg.plataformas.labs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import uvg.plataformas.labs.ui.theme.DarkBackground

@Composable
fun CryptoSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .background(DarkBackground)
            .alpha(1f)
    ) {
        content()
    }
}