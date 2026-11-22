package com.example.inspectorcomponentes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para realizar operaciones CRUD en la tabla de componentes.
 */
@Dao
interface ComponenteDao {

    @Query("SELECT * FROM componentes ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<ComponenteEntity>>

    @Query("SELECT * FROM componentes WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ComponenteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(componente: ComponenteEntity)

    @Update
    suspend fun actualizar(componente: ComponenteEntity)

    @Delete
    suspend fun eliminar(componente: ComponenteEntity)
}
