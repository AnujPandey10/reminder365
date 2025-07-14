package com.remindme365.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.remindme365.app.data.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import androidx.compose.foundation.clickable

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
    
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    if (selectedEvent != null) {
        EventDetailsScreen(
            event = selectedEvent!!,
            onBackPressed = { selectedEvent = null },
            onDeleteEvent = { eventId ->
                viewModel.deleteEvent(eventId)
                selectedEvent = null
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Upcoming Events", fontWeight = FontWeight.Bold) },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp,
                    actions = {
                        IconButton(onClick = { viewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
            }
        ) { paddingValues ->
            when (state) {
                is HomeState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is HomeState.Success -> {
                    HomeContent(
                        events = (state as HomeState.Success).events,
                        onEventClick = { event -> selectedEvent = event },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                is HomeState.Error -> {
                    ErrorContent(
                        message = (state as HomeState.Error).message,
                        onRetry = { viewModel.refresh() },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    events: List<Event>, 
    onEventClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Summary card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "You have ${events.size} upcoming events!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (events.isNotEmpty()) {
                    val nextEvent = events.first()
                    Text(
                        "Next: ${nextEvent.name}'s ${nextEvent.type.name.lowercase()} on ${formatDate(nextEvent.date)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Text(
            "Upcoming Events",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (events.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Event,
                title = "No upcoming events",
                message = "Add some birthdays and anniversaries to see them here"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event) }
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image or icon
            if (event.photoUrl != null) {
                AsyncImage(
                    model = event.photoUrl,
                    contentDescription = event.name,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                )
            } else {
                Icon(
                    imageVector = when (event.type) {
                        EventType.BIRTHDAY -> Icons.Default.Cake
                        EventType.ANNIVERSARY -> Icons.Default.Favorite
                        EventType.CUSTOM -> Icons.Default.Event
                    },
                    contentDescription = event.type.name,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatDate(event.date),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                if (event.relation.isNotEmpty()) {
                    Text(
                        event.relation,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary
                    )
                }
                if (event.notes.isNotEmpty()) {
                    Text(
                        event.notes,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
                if (event.isRecurring) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Repeat,
                            contentDescription = "Recurring",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.size(12.dp)
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
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun EventsScreen() {
    val viewModel: EventsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
    val selectedEventType by viewModel.selectedEventType.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    var showAddEvent by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var editingEvent by remember { mutableStateOf<Event?>(null) }

    if (selectedEvent != null) {
        EventDetailsScreen(
            event = selectedEvent!!,
            onBackPressed = { selectedEvent = null },
            onEditEvent = { event -> 
                editingEvent = event
                selectedEvent = null
            },
            onDeleteEvent = { eventId ->
                viewModel.deleteEvent(eventId)
                selectedEvent = null
            }
        )
    } else if (showAddEvent || editingEvent != null) {
        AddEventScreen(
            event = editingEvent, // Pass the event for editing
            onEventAdded = {
                showAddEvent = false
                editingEvent = null
                viewModel.refresh()
            },
            onBackPressed = {
                showAddEvent = false
                editingEvent = null
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Events", fontWeight = FontWeight.Bold) },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp,
                    actions = {
                        IconButton(onClick = { viewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddEvent = true },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search bar
                var searchQuery by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.search(it)
                    },
                    label = { Text("Search Events") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                )

                // Event type filter
                EventTypeFilter(
                    selectedType = selectedEventType,
                    onTypeSelected = { viewModel.selectEventType(it) }
                )

                when (state) {
                    is EventsState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is EventsState.Success -> {
                        EventsContent(
                            events = (state as EventsState.Success).events,
                            onDeleteEvent = { viewModel.deleteEvent(it) },
                            onEventClick = { event -> selectedEvent = event }
                        )
                    }
                    is EventsState.Error -> {
                        ErrorContent(
                            message = (state as EventsState.Error).message,
                            onRetry = { viewModel.refresh() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterialApi::class)
@Composable
fun EventTypeFilter(
    selectedType: EventType,
    onTypeSelected: (EventType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EventType.values().forEach { eventType ->
            FilterChip(
                selected = selectedType == eventType,
                onClick = { onTypeSelected(eventType) },
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
}

@Composable
fun EventsContent(
    events: List<Event>,
    onDeleteEvent: (String) -> Unit,
    onEventClick: (Event) -> Unit
) {
    if (events.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Event,
            title = "No events found",
            message = "Add some events to get started"
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCardWithDelete(
                    event = event,
                    onDelete = { onDeleteEvent(event.id) },
                    onClick = { onEventClick(event) }
                )
            }
        }
    }
}

@Composable
fun EventCardWithDelete(
    event: Event,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event icon
            Icon(
                imageVector = when (event.type) {
                    EventType.BIRTHDAY -> Icons.Default.Cake
                    EventType.ANNIVERSARY -> Icons.Default.Favorite
                    EventType.CUSTOM -> Icons.Default.Event
                },
                contentDescription = event.type.name,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatDate(event.date),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                if (event.relation.isNotEmpty()) {
                    Text(
                        event.relation,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun RemindersScreen() {
    val viewModel: RemindersViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    var showAddReminder by remember { mutableStateOf(false) }
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var editingReminder by remember { mutableStateOf<Reminder?>(null) }

    if (selectedReminder != null) {
        ReminderDetailsScreen(
            reminder = selectedReminder!!,
            onBackPressed = { selectedReminder = null },
            onEditReminder = { reminder -> 
                editingReminder = reminder
                selectedReminder = null
            },
            onDeleteReminder = { reminderId ->
                viewModel.deleteReminder(reminderId)
                selectedReminder = null
            }
        )
    } else if (showAddReminder || editingReminder != null) {
        AddReminderScreen(
            reminder = editingReminder, // Pass the reminder for editing
            onReminderAdded = {
                showAddReminder = false
                editingReminder = null
                viewModel.refresh()
            },
            onBackPressed = {
                showAddReminder = false
                editingReminder = null
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Reminders", fontWeight = FontWeight.Bold) },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp,
                    actions = {
                        IconButton(onClick = { viewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddReminder = true },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Reminder")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search bar
                var searchQuery by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.search(it)
                    },
                    label = { Text("Search Reminders") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                )

                when (state) {
                    is RemindersState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                is RemindersState.Success -> {
                    RemindersContent(
                        reminders = (state as RemindersState.Success).reminders,
                        onDeleteReminder = { viewModel.deleteReminder(it) },
                        onReminderClick = { reminder -> selectedReminder = reminder },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                is RemindersState.Error -> {
                    ErrorContent(
                        message = (state as RemindersState.Error).message,
                        onRetry = { viewModel.refresh() },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun RemindersContent(
    reminders: List<Reminder>,
    onDeleteReminder: (String) -> Unit,
    onReminderClick: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (reminders.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Schedule,
                title = "No reminders",
                message = "Add some reminders to get started"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reminders) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        onDelete = { onDeleteReminder(reminder.id) },
                        onClick = { onReminderClick(reminder) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReminderCard(
    reminder: Reminder,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Reminder",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reminder.title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatDateTime(reminder.dateTime),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                if (reminder.description.isNotEmpty()) {
                    Text(
                        reminder.description,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
                if (reminder.isRecurring) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Repeat,
                            contentDescription = "Recurring",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.size(12.dp)
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

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                "Settings",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Settings options
            SettingsOption(
                icon = Icons.Default.Notifications,
                title = "Notification Settings",
                subtitle = "Configure notification preferences"
            )

            SettingsOption(
                icon = Icons.Default.CloudSync,
                title = "Sync Settings",
                subtitle = "Manage cloud synchronization"
            )

            SettingsOption(
                icon = Icons.Default.ImportExport,
                title = "Import/Export",
                subtitle = "Import from calendar or contacts"
            )

            SettingsOption(
                icon = Icons.Default.Star,
                title = "Premium Features",
                subtitle = "Upgrade to premium"
            )

            SettingsOption(
                icon = Icons.Default.Help,
                title = "Help & Support",
                subtitle = "Get help and contact support"
            )
        }
    }
}

@Composable
fun SettingsOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Error",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

// Utility functions
fun formatDate(date: LocalDate): String {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val daysUntil = currentDate.daysUntil(date).toLong()
    
    return when {
        daysUntil == 0L -> "Today"
        daysUntil == 1L -> "Tomorrow"
        daysUntil < 7L -> "In $daysUntil days"
        else -> "${date.month.name.lowercase().capitalizeFirst()} ${date.dayOfMonth}"
    }
}

fun formatDateTime(dateTime: kotlinx.datetime.LocalDateTime): String {
    return "${formatDate(dateTime.date)} at ${dateTime.hour}:${dateTime.minute.toString().padStart(2, '0')}"
}