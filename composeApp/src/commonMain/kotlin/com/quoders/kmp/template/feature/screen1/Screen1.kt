package com.quoders.kmp.template.feature.screen1

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.quoders.kmp.template.Album
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun Screen1() {
    val viewModel: Screen1ViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    Screen1Content(state)
}

@Composable
fun Screen1Content(state: Screen1State) {
    when (state) {
        is Screen1State.Loading -> Text("Loading...")
        is Screen1State.Result -> Screen1Albums(state.albums)
        is Screen1State.Error -> Text("Error: ${state.error}")
    }
}

@Composable
fun Screen1Albums(albums: List<Album>) {
    LazyColumn {
        items(albums) { album ->
            AlbumRow(album)
        }
    }
}

@Composable
fun AlbumRow(album: Album) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            model = album.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = album.title,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}