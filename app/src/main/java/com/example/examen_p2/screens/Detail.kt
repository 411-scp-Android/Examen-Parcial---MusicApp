package com.example.examen_p2.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(albumId: String, onBack: () -> Unit) {
    Text(text = "Pantalla Detalle del álbum: $albumId")
}
