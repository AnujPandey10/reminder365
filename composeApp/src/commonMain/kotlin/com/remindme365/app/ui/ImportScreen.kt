package com.remindme365.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> ImportScreen(
    items: List<T>,
    title: String,
    onImport: (List<T>) -> Unit,
    onBackPressed: () -> Unit,
    itemContent: @Composable (T, Boolean, () -> Unit) -> Unit
) {
    var selectedItems by remember { mutableStateOf(emptyList<T>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onImport(selectedItems) }) {
                        Icon(Icons.Default.Done, contentDescription = "Import")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items) { item ->
                val isSelected = selectedItems.contains(item)
                itemContent(item, isSelected) {
                    selectedItems = if (isSelected) {
                        selectedItems - item
                    } else {
                        selectedItems + item
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Selected",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}
