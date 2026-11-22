package com.example.inspectorcomponentes

import android.app.Application
import com.example.inspectorcomponentes.data.local.database.AppDatabase
import com.example.inspectorcomponentes.data.repository.ComponenteRepository

/**
 * Clase de aplicación personalizada que gestiona el ciclo de vida global de la app.
 * Inicializa de forma perezosa (by lazy) la base de datos y el repositorio
 * para evitar bloqueos en el arranque inicial.
 */
class InspectorApplication : Application() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ComponenteRepository(database.componenteDao()) }

}
