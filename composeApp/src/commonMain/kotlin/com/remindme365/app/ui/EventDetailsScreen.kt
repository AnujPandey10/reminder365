package com.remindme365.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remindme365.app.data.Event
import com.remindme365.app.data.EventType
import kotlinx.datetime.LocalDate

@Composable
fun EventDetailsScreen(
    event: Event,
    onBackPressed: () -> Unit,
    onEditEvent: (Event) -> Unit = {},
    onDeleteEvent: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditEvent(event) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDeleteEvent(event.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Event Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = when (event.type) {
                            EventType.BIRTHDAY -> Icons.Default.Cake
                            EventType.ANNIVERSARY -> Icons.Default.Favorite
                            EventType.CUSTOM -> Icons.Default.Event
                        },
                        contentDescription = event.type.name,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        event.name,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    
                    Text(
                        event.type.name.lowercase().capitalizeFirst(),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                    
                    if (event.isRecurring) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Repeat,
                                contentDescription = "Recurring",
                                tint = MaterialTheme.colors.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Repeats annually",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Event Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Event Details",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Date
                    DetailRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Date",
                        value = formatDate(event.date)
                    )

                    // Relation
                    if (event.relation.isNotEmpty()) {
                        DetailRow(
                            icon = Icons.Default.People,
                            label = "Relation",
                            value = event.relation
                        )
                    }

                    // Email
                    if (event.email != null && event.email.isNotEmpty()) {
                        DetailRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = event.email
                        )
                    }

                    // Notes
                    if (event.notes.isNotEmpty()) {
                        DetailRow(
                            icon = Icons.Default.Note,
                            label = "Notes",
                            value = event.notes
                        )
                    }

                    // Notification Settings
                    DetailRow(
                        icon = Icons.Default.Notifications,
                        label = "Notification",
                        value = when (event.notificationTiming) {
                            com.remindme365.app.data.NotificationTiming.ON_DAY -> "On the day"
                            com.remindme365.app.data.NotificationTiming.DAY_BEFORE -> "Day before"
                            com.remindme365.app.data.NotificationTiming.WEEK_BEFORE -> "Week before"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onEditEvent(event) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Edit")
                }
                
                OutlinedButton(
                    onClick = { onDeleteEvent(event.id) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 12.dp)
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            Text(
                value,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium
            )
        }
    }
} 