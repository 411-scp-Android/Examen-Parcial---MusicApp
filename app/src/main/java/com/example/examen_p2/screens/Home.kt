package com.example.examen_p2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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
fun HomeScreen(onAlbumClick: (String) -> Unit) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            albums = retrofitService.getAlbums()
            isLoading = false
        } catch (_: Exception) {
            isLoading = false
            isError = true
        }
    }

    HomeContent(
        albums = albums,
        isLoading = isLoading,
        isError = isError,
        onAlbumClick = onAlbumClick
    )
}

@Composable
fun HomeContent(
    albums: List<Album>,
    isLoading: Boolean,
    isError: Boolean,
    onAlbumClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3E5F5))) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection()

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF7E57C2))
                }
            } else if (isError) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error al cargar los álbumes", color = Color.Red)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        SectionTitle(title = "Albums")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(albums) { album ->
                                AlbumCard(album = album, onClick = { onAlbumClick(album.id) })
                            }
                        }
                    }

                    item {
                        SectionTitle(title = "Recently Played")
                    }
                    items(albums.reversed()) { album ->
                        RecentlyPlayedItem(album = album, onClick = { onAlbumClick(album.id) })
                    }
                }
            }
        }

        if (albums.isNotEmpty()) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                MiniPlayer(album = albums.first())
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF9575CD), Color(0xFF7E57C2))
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Good Morning!", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
            Text(
                text = "Juan Frausto",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = "See more", color = Color(0xFF7E57C2), fontSize = 14.sp)
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = album.image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                onError = { println("Coil Error: ${it.result.throwable.message}") }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = album.title, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(text = album.artist, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, maxLines = 1)
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = album.title, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "${album.artist} • Popular Song", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockAlbums = listOf(
        Album("1", "Tales of Ithiria", "Haggard", "Description", ""),
        Album("2", "Awake", "Skillet", "Description", ""),
        Album("3", "Nightmare", "A7X", "Description", "")
    )
    HomeContent(
        albums = mockAlbums,
        isLoading = false,
        isError = false,
        onAlbumClick = {}
    )
}
