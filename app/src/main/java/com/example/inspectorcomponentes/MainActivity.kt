package com.example.inspectorcomponentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.inspectorcomponentes.ui.AppInspectorMainScreen
import com.example.inspectorcomponentes.ui.theme.InspectorComponentesTheme
import com.example.inspectorcomponentes.ui.viewmodel.ComponenteViewModel
import com.example.inspectorcomponentes.ui.viewmodel.ComponenteViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: ComponenteViewModel by viewModels {
        ComponenteViewModelFactory((application as InspectorApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InspectorComponentesTheme {
                AppInspectorMainScreen(viewModel = viewModel)
            }
        }
    }
}
