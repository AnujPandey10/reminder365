package com.remindme365.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DateTimePickerDialog(
    onDateTimeSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    var selectedYear by remember { mutableStateOf(today.date.year) }
    var selectedMonth by remember { mutableStateOf(today.date.month) }
    var selectedDay by remember { mutableStateOf(today.date.dayOfMonth) }
    var selectedHour by remember { mutableStateOf(today.time.hour) }
    var selectedMinute by remember { mutableStateOf(today.time.minute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Date & Time", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // Date Section
                Text("Date", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                
                // Year selector
                Text("Year", style = MaterialTheme.typography.caption, modifier = Modifier.padding(bottom = 4.dp))
                LazyColumn(
                    modifier = Modifier.height(80.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items((2024..2030).toList()) { year ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedYear = year },
                            shape = RoundedCornerShape(6.dp),
                            backgroundColor = if (selectedYear == year) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Text(
                                text = year.toString(),
                                modifier = Modifier.padding(8.dp),
                                color = if (selectedYear == year) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontWeight = if (selectedYear == year) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Month selector
                Text("Month", style = MaterialTheme.typography.caption, modifier = Modifier.padding(bottom = 4.dp))
                LazyColumn(
                    modifier = Modifier.height(80.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(Month.values().toList()) { month ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedMonth = month },
                            shape = RoundedCornerShape(6.dp),
                            backgroundColor = if (selectedMonth == month) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Text(
                                text = month.name.lowercase().capitalizeFirst(),
                                modifier = Modifier.padding(8.dp),
                                color = if (selectedMonth == month) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontWeight = if (selectedMonth == month) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day selector
                Text("Day", style = MaterialTheme.typography.caption, modifier = Modifier.padding(bottom = 4.dp))
                LazyColumn(
                    modifier = Modifier.height(80.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items((1..31).toList()) { day ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDay = day },
                            shape = RoundedCornerShape(6.dp),
                            backgroundColor = if (selectedDay == day) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Text(
                                text = day.toString(),
                                modifier = Modifier.padding(8.dp),
                                color = if (selectedDay == day) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontWeight = if (selectedDay == day) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time Section
                Text("Time", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Hour selector
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Hour", style = MaterialTheme.typography.caption, modifier = Modifier.padding(bottom = 4.dp))
                        LazyColumn(
                            modifier = Modifier.height(80.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items((0..23).toList()) { hour ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedHour = hour },
                                    shape = RoundedCornerShape(6.dp),
                                    backgroundColor = if (selectedHour == hour) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                                    elevation = 1.dp
                                ) {
                                    Text(
                                        text = hour.toString().padStart(2, '0'),
                                        modifier = Modifier.padding(8.dp),
                                        color = if (selectedHour == hour) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                        fontWeight = if (selectedHour == hour) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Minute selector
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Minute", style = MaterialTheme.typography.caption, modifier = Modifier.padding(bottom = 4.dp))
                        LazyColumn(
                            modifier = Modifier.height(80.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items((0..59).toList()) { minute ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedMinute = minute },
                                    shape = RoundedCornerShape(6.dp),
                                    backgroundColor = if (selectedMinute == minute) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                                    elevation = 1.dp
                                ) {
                                    Text(
                                        text = minute.toString().padStart(2, '0'),
                                        modifier = Modifier.padding(8.dp),
                                        color = if (selectedMinute == minute) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                        fontWeight = if (selectedMinute == minute) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    try {
                        val selectedDate = LocalDate(selectedYear, selectedMonth, selectedDay)
                        val selectedTime = LocalTime(selectedHour, selectedMinute)
                        val selectedDateTime = LocalDateTime(selectedDate, selectedTime)
                        onDateTimeSelected(selectedDateTime)
                        onDismiss()
                    } catch (e: Exception) {
                        // Handle invalid date/time
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 