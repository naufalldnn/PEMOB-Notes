package com.naufal.mynote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.mynote.data.NoteDatabase
import com.naufal.mynote.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NoteDatabase.getDatabase(application).dao

    val notes: StateFlow<List<Note>> = dao.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getNoteById(id: Long): Note? {
        return notes.value.find { it.id == id }
    }

    fun saveNote(
        id: Long? = null,
        content: String,
        color: Long = 0xFFFFF9C4,
        isPinned: Boolean = false
    ) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val existingNote = id?.let { dao.getNoteById(it) }
            val note = Note(
                id = id ?: 0,
                content = content.trim(),
                color = color,
                isPinned = isPinned,
                createdAt = existingNote?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            dao.upsertNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            dao.deleteNoteById(id)
        }
    }
}
