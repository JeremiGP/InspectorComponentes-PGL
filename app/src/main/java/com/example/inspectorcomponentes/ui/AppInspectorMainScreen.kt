package com.example.inspectorcomponentes.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inspectorcomponentes.R
import com.example.inspectorcomponentes.ui.navigation.Ruta
import com.example.inspectorcomponentes.ui.screen.AjustesScreen
import com.example.inspectorcomponentes.ui.screen.CatalogoScreen
import com.example.inspectorcomponentes.ui.screen.DetalleComponenteScreen
import com.example.inspectorcomponentes.ui.screen.EscanerScreen
import com.example.inspectorcomponentes.ui.screen.FormularioComponenteScreen
import com.example.inspectorcomponentes.ui.screen.InicioScreen
import com.example.inspectorcomponentes.ui.viewmodel.ComponenteViewModel
import com.example.inspectorcomponentes.ui.viewmodel.DetalleComponenteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInspectorMainScreen(
    viewModel: ComponenteViewModel,
    detalleViewModel: DetalleComponenteViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // Determinamos el título según la ruta actual
    val titleResId = when {
        currentRoute == Ruta.Inicio.route -> R.string.title_inicio
        currentRoute == Ruta.Catalogo.route -> R.string.title_catalogo
        currentRoute == Ruta.Formulario.route -> R.string.title_nuevo
        currentRoute?.startsWith("detalle/") == true -> R.string.title_detalle
        currentRoute == Ruta.Escaner.route -> R.string.title_escaner
        currentRoute == Ruta.Ajustes.route -> R.string.title_ajustes
        else -> R.string.app_name
    }

    // Mostramos botón "Volver" solo en pantallas hijas (Detalle)
    val showBackButton = currentRoute?.startsWith("detalle/") == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = titleResId)) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.desc_icono_volver)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            // Se muestra el BottomBar solo en los destinos raíz
            val isMainRoute = currentRoute in listOf(
                Ruta.Inicio.route,
                Ruta.Catalogo.route,
                Ruta.Formulario.route,
                Ruta.Escaner.route,
                Ruta.Ajustes.route
            )

            if (isMainRoute) {
                NavigationBar {
                    val items = listOf<Triple<String, Int, ImageVector>>(
                        Triple(Ruta.Inicio.route, R.string.nav_inicio, Icons.Default.Home),
                        Triple(Ruta.Catalogo.route, R.string.nav_catalogo, Icons.Default.List),
                        Triple(Ruta.Formulario.route, R.string.nav_nuevo, Icons.Default.Add),
                        Triple(Ruta.Escaner.route, R.string.nav_escaner, Icons.Default.Search),
                        Triple(Ruta.Ajustes.route, R.string.nav_ajustes, Icons.Default.Settings)
                    )

                    items.forEach { (route, labelResId, icon) ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == route } == true
                        NavigationBarItem(
                            icon = { Icon(imageVector = icon, contentDescription = stringResource(id = labelResId)) },
                            label = { Text(text = stringResource(id = labelResId)) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(route) {
                                    // Evitar la acumulación de backstack volviendo al inicio
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Evitar instanciar la misma pantalla múltiple veces
                                    launchSingleTop = true
                                    // Restaurar el estado previo de la pestaña
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Ruta.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Ruta.Inicio.route) {
                InicioScreen(
                    viewModel = viewModel,
                    onNavigateToCatalogo = { navController.navigate(Ruta.Catalogo.route) },
                    onNavigateToNuevo = { navController.navigate(Ruta.Formulario.route) }
                )
            }
            composable(Ruta.Catalogo.route) {
                CatalogoScreen(
                    viewModel = viewModel,
                    onComponenteClick = { id -> navController.navigate(Ruta.Detalle.createRoute(id)) }
                )
            }
            composable(Ruta.Formulario.route) {
                FormularioComponenteScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Ruta.Escaner.route) {
                EscanerScreen()
            }
            composable(Ruta.Ajustes.route) {
                AjustesScreen(viewModel = viewModel)
            }
            composable(
                route = Ruta.Detalle.route,
                arguments = listOf(navArgument("componenteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("componenteId") ?: 0
                DetalleComponenteScreen(
                    idComponente = id,
                    viewModel = detalleViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
