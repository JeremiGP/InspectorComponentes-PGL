package com.example.inspectorcomponentes.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inspectorcomponentes.data.local.dao.ComponenteDao
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity

/**
 * Base de datos principal de la aplicación.
 * Implementa el patrón Singleton usando @Volatile y synchronized para asegurar
 * que solo exista una instancia activa.
 */
@Database(entities = [ComponenteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun componenteDao(): ComponenteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inspector_componentes_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
