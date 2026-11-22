package com.example.inspectorcomponentes.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inspectorcomponentes.R
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity
import com.example.inspectorcomponentes.viewmodel.ComponenteViewModel

@Composable
fun FormularioComponenteScreen(
    viewModel: ComponenteViewModel,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var encapsulado by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var esSmd by remember { mutableStateOf(true) }

    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (errorMensaje.isNotBlank()) {
            Text(
                text = errorMensaje,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre (ej. NE555)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = tipo,
            onValueChange = { tipo = it },
            label = { Text("Tipo (ej. IC, Resistor)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = encapsulado,
            onValueChange = { encapsulado = it },
            label = { Text("Encapsulado (ej. SOIC-8, 1206)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Valor (ej. 10K, Temporizador)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción o notas") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = esSmd,
                onCheckedChange = { esSmd = it }
            )
            Text(text = "¿Es un componente SMD (Surface-Mount Device)?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isBlank() || tipo.isBlank()) {
                    errorMensaje = "El nombre y el tipo son obligatorios"
                } else {
                    val nuevoComponente = ComponenteEntity(
                        nombre = nombre,
                        tipo = tipo,
                        encapsulado = encapsulado,
                        valor = valor,
                        descripcion = descripcion,
                        esSmd = esSmd
                    )
                    viewModel.agregarComponente(nuevoComponente)
                    onNavigateBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.btn_guardar),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
