package com.remindme365.app.data

import android.content.ContentResolver
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class ContactReader(private val contentResolver: ContentResolver) {
    actual suspend fun getContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var birthday: String? = null

                val bdayCursor = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = ?",
                    arrayOf(id, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY.toString()),
                    null
                )

                bdayCursor?.use { bCursor ->
                    if (bCursor.moveToFirst()) {
                        birthday = bCursor.getString(bCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                    }
                }

                contacts.add(Contact(id, name, birthday))
            }
        }
        contacts
    }
}
