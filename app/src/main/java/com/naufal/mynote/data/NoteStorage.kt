package com.naufal.mynote.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.naufal.mynote.model.Note

class NoteStorage(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("note_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getAllNotes(): List<Note> {
        val notesJson = sharedPreferences.getString("notes", null) ?: return emptyList()
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(notesJson, type)
    }

    fun saveNotes(notes: List<Note>) {
        val notesJson = gson.toJson(notes)
        sharedPreferences.edit().putString("notes", notesJson).apply()
    }
}