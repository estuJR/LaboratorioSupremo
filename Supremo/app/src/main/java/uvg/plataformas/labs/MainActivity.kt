package uvg.plataformas.labs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uvg.plataformas.labs.data.local.entity.AppDatabase
import uvg.plataformas.labs.data.remote.ApiService
import uvg.plataformas.labs.presentation.navigation.AppNavigation
import uvg.plataformas.labs.presentation.screen1.Screen1ViewModel
import uvg.plataformas.labs.presentation.screen2.Screen2ViewModel
import uvg.plataformas.labs.ui.theme.LabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val apiService = ApiService()
        val usuarioDao = database.usuarioDao()

        val screen1ViewModel = Screen1ViewModel(apiService, usuarioDao)
        val screen2ViewModel = Screen2ViewModel(apiService, usuarioDao)

        setContent {
            LabTheme {
                AppNavigation(
                    screen1ViewModel = screen1ViewModel,
                    screen2ViewModel = screen2ViewModel
                )
            }
        }
    }
}