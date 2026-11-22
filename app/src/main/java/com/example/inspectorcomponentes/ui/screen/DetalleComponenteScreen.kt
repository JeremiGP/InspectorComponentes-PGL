package com.example.inspectorcomponentes.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inspectorcomponentes.R
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity
import com.example.inspectorcomponentes.ui.state.DetalleUiState
import com.example.inspectorcomponentes.ui.viewmodel.DetalleComponenteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DetalleComponenteScreen(
    idComponente: Int,
    viewModel: DetalleComponenteViewModel,
    onNavigateBack: () -> Unit
) {
    // Al abrir la pantalla, se lanza la carga asíncrona usando Dispatchers.IO
    // tal como pide la documentación técnica.
    LaunchedEffect(idComponente) {
        withContext(Dispatchers.IO) {
            viewModel.cargarComponente(idComponente)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is DetalleUiState.Cargando -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DetalleUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is DetalleUiState.Exito -> {
                ContenidoDetalle(
                    componente = state.componente,
                    onEliminarClick = {
                        viewModel.eliminar(state.componente)
                        onNavigateBack()
                    }
                )
            }
        }
    }
}

@Composable
fun ContenidoDetalle(
    componente: ComponenteEntity,
    onEliminarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = componente.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                DetalleFila(label = "Tipo:", valor = componente.tipo)
                DetalleFila(label = "Encapsulado:", valor = componente.encapsulado)
                DetalleFila(label = "Valor:", valor = componente.valor)
                DetalleFila(label = "SMD:", valor = if (componente.esSmd) "Sí" else "No")
                DetalleFila(label = "Verificado:", valor = if (componente.verificado) "Sí" else "No")

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Descripción:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = componente.descripcion.ifBlank { "Sin descripción" },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onEliminarClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = stringResource(id = R.string.btn_eliminar),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}

@Composable
fun DetalleFila(label: String, valor: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
        Text(text = valor, style = MaterialTheme.typography.bodyLarge)
    }
}