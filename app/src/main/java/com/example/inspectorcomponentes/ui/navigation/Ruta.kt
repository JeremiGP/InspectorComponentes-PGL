package com.example.inspectorcomponentes.ui.navigation

/**
 * Define las rutas de navegación tipadas de la aplicación, evitando Strings hardcoded
 * dispersos por el código y garantizando la seguridad de tipos.
 */
sealed class Ruta(val route: String) {
    data object Inicio : Ruta("inicio")
    data object Catalogo : Ruta("catalogo")
    data object Formulario : Ruta("formulario")
    data object Escaner : Ruta("escaner")
    data object Ajustes : Ruta("ajustes")

    // Ruta Maestro-Detalle con argumento dinámico
    data object Detalle : Ruta("detalle/{componenteId}") {
        fun createRoute(componenteId: Int): String {
            return "detalle/$componenteId"
        }
    }
}
