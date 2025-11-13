package uvg.plataformas.labs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import uvg.plataformas.labs.presentation.screen1.Screen1Route
import uvg.plataformas.labs.presentation.screen1.Screen1ViewModel
import uvg.plataformas.labs.presentation.screen2.Screen2Route
import uvg.plataformas.labs.presentation.screen2.Screen2ViewModel

@Composable
fun AppNavigation(
    screen1ViewModel: Screen1ViewModel,
    screen2ViewModel: Screen2ViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "screen1"
    ) {

        composable("screen1") {
            Screen1Route(
                viewModel = screen1ViewModel,
                onNavigateToDetail = { id ->
                    navController.navigate("screen2/$id")
                }
            )
        }

        composable(
            route = "screen2/{assetId}",
            arguments = listOf(
                navArgument("assetId") { type = NavType.StringType }
            )
        ) { entry ->
            val id = entry.arguments?.getString("assetId") ?: ""

            Screen2Route(
                assetId = id,
                viewModel = screen2ViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}