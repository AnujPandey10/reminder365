package com.remindme365.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var selectedYear by remember { mutableStateOf(today.year) }
    var selectedMonth by remember { mutableStateOf(today.month) }
    var selectedDay by remember { mutableStateOf(today.dayOfMonth) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Date", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // Year selector
                Text("Year", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                LazyColumn(
                    modifier = Modifier.height(120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items((2020..2030).toList()) { year ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedYear = year },
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = if (selectedYear == year) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Text(
                                text = year.toString(),
                                modifier = Modifier.padding(12.dp),
                                color = if (selectedYear == year) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontWeight = if (selectedYear == year) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Month selector
                Text("Month", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                LazyColumn(
                    modifier = Modifier.height(120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(Month.values().toList()) { month ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedMonth = month },
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = if (selectedMonth == month) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Text(
                                text = month.name.lowercase().capitalizeFirst(),
                                modifier = Modifier.padding(12.dp),
                                color = if (selectedMonth == month) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontWeight = if (selectedMonth == month) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Day selector
                Text("Day", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                LazyColumn(
                    modifier = Modifier.height(120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items((1..31).toList()) { day ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDay = day },
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = if (selectedDay == day) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Text(
                                text = day.toString(),
                                modifier = Modifier.padding(12.dp),
                                color = if (selectedDay == day) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontWeight = if (selectedDay == day) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(LocalDate(selectedYear, selectedMonth, selectedDay))
                    onDismiss()
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