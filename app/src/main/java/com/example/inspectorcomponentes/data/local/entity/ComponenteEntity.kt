package com.example.inspectorcomponentes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un componente electrónico (ej. SMD) en la base de datos local.
 */
@Entity(tableName = "componentes")
data class ComponenteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val encapsulado: String, // ej. 0805, 1206, SOIC-8
    val valor: String,
    val descripcion: String,
    val esSmd: Boolean = true,
    val verificado: Boolean = false,
    val favorito: Boolean = false
)
