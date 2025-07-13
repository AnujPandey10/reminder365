package com.remindme365.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.remindme365.app.data.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterialApi::class)
@Composable
fun AddReminderScreen(
    reminder: Reminder? = null, // For editing existing reminder
    onReminderAdded: () -> Unit,
    onBackPressed: () -> Unit
) {
    object : KoinComponent {}.apply {
        val repository: Repository by inject()
        val remindersViewModel: RemindersViewModel by inject()
        
        var title by remember { mutableStateOf(reminder?.title ?: "") }
        var description by remember { mutableStateOf(reminder?.description ?: "") }
        var selectedNotificationTiming by remember { mutableStateOf(reminder?.notificationTiming ?: NotificationTiming.ON_DAY) }
        var selectedNotificationType by remember { mutableStateOf(reminder?.notificationType ?: NotificationType.APP_POPUP) }
        var selectedDateTime by remember { 
            mutableStateOf(
                reminder?.dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            ) 
        }
        var showDateTimePicker by remember { mutableStateOf(false) }
        var isSaving by remember { mutableStateOf(false) }
        var isRecurring by remember { mutableStateOf(reminder?.isRecurring ?: false) }
        var selectedRecurringInterval by remember { mutableStateOf(reminder?.recurringInterval ?: RecurringInterval.DAILY) }

        if (showDateTimePicker) {
            DateTimePickerDialog(
                onDateTimeSelected = { selectedDateTime = it },
                onDismiss = { showDateTimePicker = false }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            if (reminder == null) "Add Reminder" else "Edit Reminder", 
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                // Save reminder
                                isSaving = true
                                val reminderToSave = if (reminder == null) {
                                    // Create new reminder
                                    Reminder(
                                        title = title,
                                        description = description,
                                        dateTime = selectedDateTime,
                                        notificationTiming = selectedNotificationTiming,
                                        notificationType = selectedNotificationType,
                                        isRecurring = isRecurring,
                                        recurringInterval = if (isRecurring) selectedRecurringInterval else null
                                    )
                                } else {
                                    // Update existing reminder
                                    reminder.copy(
                                        title = title,
                                        description = description,
                                        dateTime = selectedDateTime,
                                        notificationTiming = selectedNotificationTiming,
                                        notificationType = selectedNotificationType,
                                        isRecurring = isRecurring,
                                        recurringInterval = if (isRecurring) selectedRecurringInterval else null
                                    )
                                }
                                
                                // Save reminder in a coroutine
                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        if (reminder == null) {
                                            repository.saveReminder(reminderToSave)
                                        } else {
                                            repository.updateReminder(reminderToSave)
                                        }
                                        remindersViewModel.refresh()
                                        onReminderAdded()
                                    } catch (e: Exception) {
                                        // Handle error
                                        isSaving = false
                                    }
                                }
                            },
                            enabled = title.isNotEmpty() && !isSaving
                        ) {
                            Text("Save", color = MaterialTheme.colors.primary)
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
                // Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Title, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    leadingIcon = {
                        Icon(Icons.Default.Description, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Date & Time Selection
                Text(
                    "Date & Time",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedButton(
                    onClick = { showDateTimePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(formatDateTime(selectedDateTime))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Notification Settings
                Text(
                    "Notification Settings",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Notification Timing
                Text(
                    "When to notify:",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NotificationTiming.values().forEach { timing ->
                        FilterChip(
                            selected = selectedNotificationTiming == timing,
                            onClick = { selectedNotificationTiming = timing }
                        ) {
                            Text(
                                when (timing) {
                                    NotificationTiming.ON_DAY -> "On time"
                                    NotificationTiming.DAY_BEFORE -> "Day before"
                                    NotificationTiming.WEEK_BEFORE -> "Week before"
                                },
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notification Type
                Text(
                    "Notification type:",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NotificationType.values().forEach { type ->
                        FilterChip(
                            selected = selectedNotificationType == type,
                            onClick = { selectedNotificationType = type }
                        ) {
                            Text(
                                when (type) {
                                    NotificationType.APP_POPUP -> "App popup"
                                    NotificationType.EMAIL -> "Email"
                                    NotificationType.BOTH -> "Both"
                                },
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Recurring Settings
                Text(
                    "Recurring Reminder",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Repeat reminder",
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Set up recurring notifications",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.Gray
                                )
                            }
                            Switch(
                                checked = isRecurring,
                                onCheckedChange = { isRecurring = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colors.primary,
                                    checkedTrackColor = MaterialTheme.colors.primary.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
                        if (isRecurring) {
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                "Repeat interval:",
                                style = MaterialTheme.typography.subtitle2,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RecurringInterval.values().forEach { interval ->
                                    FilterChip(
                                        selected = selectedRecurringInterval == interval,
                                        onClick = { selectedRecurringInterval = interval }
                                    ) {
                                        Text(
                                            when (interval) {
                                                RecurringInterval.DAILY -> "Daily"
                                                RecurringInterval.WEEKLY -> "Weekly"
                                                RecurringInterval.MONTHLY -> "Monthly"
                                                RecurringInterval.YEARLY -> "Yearly"
                                            },
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = {
                        // Save reminder
                        isSaving = true
                        val reminderToSave = if (reminder == null) {
                            // Create new reminder
                            Reminder(
                                title = title,
                                description = description,
                                dateTime = selectedDateTime,
                                notificationTiming = selectedNotificationTiming,
                                notificationType = selectedNotificationType,
                                isRecurring = isRecurring,
                                recurringInterval = if (isRecurring) selectedRecurringInterval else null
                            )
                        } else {
                            // Update existing reminder
                            reminder.copy(
                                title = title,
                                description = description,
                                dateTime = selectedDateTime,
                                notificationTiming = selectedNotificationTiming,
                                notificationType = selectedNotificationType,
                                isRecurring = isRecurring,
                                recurringInterval = if (isRecurring) selectedRecurringInterval else null
                            )
                        }
                        
                        // Save reminder in a coroutine
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                if (reminder == null) {
                                    repository.saveReminder(reminderToSave)
                                } else {
                                    repository.updateReminder(reminderToSave)
                                }
                                remindersViewModel.refresh()
                                onReminderAdded()
                            } catch (e: Exception) {
                                // Handle error
                                isSaving = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotEmpty() && !isSaving,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Icon(
                        Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(if (reminder == null) "Save Reminder" else "Update Reminder")
                }
            }
        }
    }
} 