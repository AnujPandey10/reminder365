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
import com.remindme365.app.data.Reminder
import com.remindme365.app.data.RecurringInterval

@Composable
fun ReminderDetailsScreen(
    reminder: Reminder,
    onBackPressed: () -> Unit,
    onEditReminder: (Reminder) -> Unit = {},
    onDeleteReminder: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reminder Details", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditReminder(reminder) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDeleteReminder(reminder.id) }) {
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
            // Reminder Header
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
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Reminder",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        reminder.title,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    
                    Text(
                        "Reminder",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                    
                    if (reminder.isRecurring) {
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
                                "Repeats ${reminder.recurringInterval?.name?.lowercase() ?: "regularly"}",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reminder Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Reminder Details",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Date & Time
                    DetailRow(
                        icon = Icons.Default.Schedule,
                        label = "Date & Time",
                        value = formatDateTime(reminder.dateTime)
                    )

                    // Description
                    if (reminder.description.isNotEmpty()) {
                        DetailRow(
                            icon = Icons.Default.Description,
                            label = "Description",
                            value = reminder.description
                        )
                    }

                    // Notification Settings
                    DetailRow(
                        icon = Icons.Default.Notifications,
                        label = "Notification",
                        value = when (reminder.notificationTiming) {
                            com.remindme365.app.data.NotificationTiming.ON_DAY -> "On time"
                            com.remindme365.app.data.NotificationTiming.DAY_BEFORE -> "Day before"
                            com.remindme365.app.data.NotificationTiming.WEEK_BEFORE -> "Week before"
                        }
                    )

                    // Notification Type
                    DetailRow(
                        icon = Icons.Default.NotificationsActive,
                        label = "Notification Type",
                        value = when (reminder.notificationType) {
                            com.remindme365.app.data.NotificationType.APP_POPUP -> "App popup"
                            com.remindme365.app.data.NotificationType.EMAIL -> "Email"
                            com.remindme365.app.data.NotificationType.BOTH -> "Both"
                        }
                    )

                    // Recurring Settings
                    if (reminder.isRecurring) {
                        DetailRow(
                            icon = Icons.Default.Repeat,
                            label = "Recurring",
                            value = when (reminder.recurringInterval) {
                                RecurringInterval.DAILY -> "Daily"
                                RecurringInterval.WEEKLY -> "Weekly"
                                RecurringInterval.MONTHLY -> "Monthly"
                                RecurringInterval.YEARLY -> "Yearly"
                                null -> "Regular"
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onEditReminder(reminder) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Edit")
                }
                
                OutlinedButton(
                    onClick = { onDeleteReminder(reminder.id) },
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