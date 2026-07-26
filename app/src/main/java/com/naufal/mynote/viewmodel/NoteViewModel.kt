package com.naufal.mynote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.mynote.data.NoteStorage
import com.naufal.mynote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = NoteStorage(application)
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _notes.value = storage.getAllNotes().sortedWith(
            compareByDescending<Note> { it.isPinned }.thenByDescending { it.updatedAt }
        )
    }

    fun getNoteById(id: Long): Note? {
        return _notes.value.find { it.id == id }
    }

    fun saveNote(
        id: Long? = null,
        content: String,
        color: Long = 0xFFFFF9C4,
        isPinned: Boolean = false
    ) {
        if (content.isBlank()) return

        val currentNotes = _notes.value.toMutableList()
        val existingNoteIndex = id?.let { targetId -> currentNotes.indexOfFirst { it.id == targetId } } ?: -1

        if (existingNoteIndex != -1) {
            val existingNote = currentNotes[existingNoteIndex]
            currentNotes[existingNoteIndex] = existingNote.copy(
                content = content.trim(),
                color = color,
                isPinned = isPinned,
                updatedAt = System.currentTimeMillis()
            )
        } else {
            val newId = if (currentNotes.isEmpty()) 1L else currentNotes.maxOf { it.id } + 1
            val newNote = Note(
                id = newId,
                content = content.trim(),
                color = color,
                isPinned = isPinned,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            currentNotes.add(newNote)
        }

        storage.saveNotes(currentNotes)
        loadNotes()
    }

    fun deleteNote(id: Long) {
        val currentNotes = _notes.value.toMutableList()
        currentNotes.removeAll { it.id == id }
        storage.saveNotes(currentNotes)
        loadNotes()
    }
}