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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remindme365.app.data.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterialApi::class)
@Composable
fun AddEventScreen(
    onEventAdded: () -> Unit,
    onBackPressed: () -> Unit
) {
    object : KoinComponent {}.apply {
        val repository: Repository by inject()
        val homeViewModel: HomeViewModel by inject()
        var name by remember { mutableStateOf("") }
        var relation by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var selectedEventType by remember { mutableStateOf(EventType.BIRTHDAY) }
        var selectedNotificationTiming by remember { mutableStateOf(NotificationTiming.ON_DAY) }
        var selectedNotificationType by remember { mutableStateOf(NotificationType.APP_POPUP) }
        var selectedDate by remember { 
            mutableStateOf(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            ) 
        }
        var showDatePicker by remember { mutableStateOf(false) }
        var isSaving by remember { mutableStateOf(false) }
        var isRecurring by remember { mutableStateOf(false) }

        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { selectedDate = it },
                onDismiss = { showDatePicker = false }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Event", fontWeight = FontWeight.Bold) },
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
                                // Save event
                                isSaving = true
                                // Create and save the event
                                val event = Event(
                                    name = name,
                                    date = selectedDate,
                                    type = selectedEventType,
                                    relation = relation,
                                    notes = notes,
                                    email = email.takeIf { it.isNotEmpty() },
                                    notificationTiming = selectedNotificationTiming,
                                    notificationType = selectedNotificationType,
                                    isRecurring = isRecurring
                                )
                                
                                // Save event in a coroutine
                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        repository.saveEvent(event)
                                        homeViewModel.onEventAdded() // Refresh home screen
                                        onEventAdded()
                                    } catch (e: Exception) {
                                        // Handle error - you might want to show a snackbar here
                                        isSaving = false
                                    }
                                }
                            },
                            enabled = name.isNotEmpty() && !isSaving
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
                // Event Type Selection
                Text(
                    "Event Type",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventType.values().forEach { eventType ->
                        FilterChip(
                            selected = selectedEventType == eventType,
                            onClick = { selectedEventType = eventType },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (eventType) {
                                        EventType.BIRTHDAY -> Icons.Default.Cake
                                        EventType.ANNIVERSARY -> Icons.Default.Favorite
                                        EventType.CUSTOM -> Icons.Default.Event
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        ) {
                            Text(
                                eventType.name.lowercase().capitalizeFirst(),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Date Selection
                Text(
                    "Date",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("${selectedDate.month.name.lowercase().capitalizeFirst()} ${selectedDate.dayOfMonth}, ${selectedDate.year}")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Relation Field
                OutlinedTextField(
                    value = relation,
                    onValueChange = { relation = it },
                    label = { Text("Relation (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.People, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notes Field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    leadingIcon = {
                        Icon(Icons.Default.Note, contentDescription = null)
                    }
                )

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
                                    NotificationTiming.ON_DAY -> "On day"
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

                // Recurring Event Settings
                Text(
                    "Recurring Event",
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
                                    "Repeat annually",
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Get notified every year on this date",
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "This event will automatically repeat every year",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = {
                        // Save event
                        isSaving = true
                        // Create and save the event
                        val event = Event(
                            name = name,
                            date = selectedDate,
                            type = selectedEventType,
                            relation = relation,
                            notes = notes,
                            email = email.takeIf { it.isNotEmpty() },
                            notificationTiming = selectedNotificationTiming,
                            notificationType = selectedNotificationType,
                            isRecurring = isRecurring
                        )
                        
                        // Save event in a coroutine
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                repository.saveEvent(event)
                                homeViewModel.onEventAdded() // Refresh home screen
                                onEventAdded()
                            } catch (e: Exception) {
                                // Handle error - you might want to show a snackbar here
                                isSaving = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty() && !isSaving,
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
                    Text("Save Event")
                }
            }
        }
    }
} 