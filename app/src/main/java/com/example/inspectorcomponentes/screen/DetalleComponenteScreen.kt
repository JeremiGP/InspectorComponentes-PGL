package com.example.inspectorcomponentes.screen

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
import com.example.inspectorcomponentes.viewmodel.state.DetalleUiState
import com.example.inspectorcomponentes.viewmodel.DetalleComponenteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DetalleComponenteScreen(
    idComponente: Int,
    viewModel: DetalleComponenteViewModel,
    onNavigateBack: () -> Unit
) {
    val msgErrorNotFound = stringResource(id = R.string.error_componente_no_encontrado)
    val msgErrorUnknown = stringResource(id = R.string.error_desconocido)

    // Al abrir la pantalla, se lanza la carga asíncrona usando Dispatchers.IO
    // tal como pide la documentación técnica.
    LaunchedEffect(idComponente) {
        withContext(Dispatchers.IO) {
            // Pasamos los strings externalizados al cargar
            viewModel.cargarComponente(
                id = idComponente,
                msgErrorNotFound = msgErrorNotFound,
                msgErrorUnknown = msgErrorUnknown
            )
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
                DetalleFila(label = stringResource(id = R.string.detalle_label_tipo), valor = componente.tipo)
                DetalleFila(label = stringResource(id = R.string.detalle_label_encapsulado), valor = componente.encapsulado)
                DetalleFila(label = stringResource(id = R.string.detalle_label_valor), valor = componente.valor)
                DetalleFila(label = stringResource(id = R.string.detalle_label_smd), valor = if (componente.esSmd) stringResource(id = R.string.detalle_si) else stringResource(id = R.string.detalle_no))
                DetalleFila(label = stringResource(id = R.string.detalle_label_verificado), valor = if (componente.verificado) stringResource(id = R.string.detalle_si) else stringResource(id = R.string.detalle_no))

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.detalle_label_desc),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = componente.descripcion.ifBlank { stringResource(id = R.string.detalle_desc_vacia) },
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