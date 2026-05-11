package com.example.examen_p2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.examen_p2.components.MiniPlayer
import com.example.examen_p2.models.Album
import com.example.examen_p2.services.retrofitService

@Composable
fun DetailScreen(albumId: String, onBack: () -> Unit) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(albumId) {
        try {
            album = retrofitService.getAlbumById(albumId)
            isLoading = false
        } catch (_: Exception) {
            isLoading = false
            isError = true
        }
    }

    DetailContent(
        album = album,
        isLoading = isLoading,
        isError = isError,
        onBack = onBack
    )
}

@Composable
fun DetailContent(
    album: Album?,
    isLoading: Boolean,
    isError: Boolean,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE1BEE7))) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7E57C2))
            }
        } else if (isError || album == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error al cargar el detalle del álbum", color = Color.Red)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header con imagen y scrim
                AlbumHeader(album = album, onBack = onBack)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, bottom = 100.dp)
                ) {
                    // About this album
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "About this album",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF311B92)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = album.description,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    // Chip del artista
                    item {
                        SuggestionChip(
                            onClick = { },
                            label = { Text("Artist: ${album.artist}") },
                            shape = CircleShape,
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.White
                            ),
                            border = null
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Lista ficticia de 10 canciones
                    items(List(10) { it + 1 }) { index ->
                        TrackItem(album = album, trackNumber = index)
                    }
                }
            }

            // Mini Reproductor Inferior (Reutilizado del estilo de Home)
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                MiniPlayer(album = album)
            }
        }
    }
}

@Composable
fun AlbumHeader(album: Album, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Scrim morado degradado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF311B92).copy(alpha = 0.8f))
                    )
                )
        )

        // Botones superiores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
            IconButton(
                onClick = { },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
            }
        }

        // Info del album y botones de accion
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text(
                text = album.title,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = album.artist,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                var isPlaying by remember { mutableStateOf(false) }
                Button(
                    onClick = { isPlaying = !isPlaying },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                OutlinedButton(
                    onClick = { },
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color.White, Color.White))),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Shuffle, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun TrackItem(album: Album, trackNumber: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = null,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${album.title} • Track $trackNumber", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = album.artist, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val mockAlbum = Album("1", "Tales of Ithiria", "Haggard", "Un álbum sinfónico que mezcla elementos de música clásica con death metal melódico.", "")
    DetailContent(
        album = mockAlbum,
        isLoading = false,
        isError = false,
        onBack = {}
    )
}
