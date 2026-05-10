package com.example.examen_p2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.examen_p2.screens.DetailScreen
import com.example.examen_p2.screens.HomeScreen
import com.example.examen_p2.ui.theme.Examen_P2Theme
import kotlinx.serialization.Serializable

// Paso 3: Rutas de navegación con Serializable
@Serializable
object HomeRoute

@Serializable
data class DetailRoute(val albumId: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Examen_P2Theme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    onAlbumClick = { id ->
                        navController.navigate(DetailRoute(albumId = id))
                    }
                )
            }
            composable<DetailRoute> { backStackEntry ->
                val detail: DetailRoute = backStackEntry.toRoute()
                DetailScreen(
                    albumId = detail.albumId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
