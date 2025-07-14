package com.remindme365.app.data

expect class ContactReader {
    suspend fun getContacts(): List<Contact>
}

data class Contact(
    val id: String,
    val name: String,
    val birthday: String?
)
