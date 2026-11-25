package com.example.inspectorcomponentes.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
fun CatalogoScreen(
    viewModel: ComponenteViewModel,
    onComponenteClick: (Int) -> Unit
) {
    val componentes by viewModel.componentes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredComponentes = if (searchQuery.isBlank()) {
        componentes
    } else {
        componentes.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) ||
            it.valor.contains(searchQuery, ignoreCase = true) ||
            it.tipo.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.catalogo_buscar_hint)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(id = R.string.desc_icono_buscar))
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(8.dp))

        if (filteredComponentes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.catalogo_vacio),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredComponentes, key = { it.id }) { componente ->
                    ComponenteCard(
                        componente = componente,
                        onClick = { onComponenteClick(componente.id) },
                        onFavoritoChange = { isFav ->
                            viewModel.actualizarComponente(componente.copy(favorito = isFav))
                        },
                        onVerificadoChange = { isVerified ->
                            viewModel.actualizarComponente(componente.copy(verificado = isVerified))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ComponenteCard(
    componente: ComponenteEntity,
    onClick: () -> Unit,
    onFavoritoChange: (Boolean) -> Unit,
    onVerificadoChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = componente.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${componente.tipo} | ${componente.valor} | ${componente.encapsulado}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = componente.verificado,
                    onCheckedChange = onVerificadoChange
                )
                IconButton(onClick = { onFavoritoChange(!componente.favorito) }) {
                    Icon(
                        imageVector = if (componente.favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (componente.favorito) stringResource(id = R.string.desc_icono_fav_remove) else stringResource(id = R.string.desc_icono_fav_add),
                        tint = if (componente.favorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
