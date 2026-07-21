package com.syaiful.mynotemaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.syaiful.mynotemaster.data.NoteDatabase
import com.syaiful.mynotemaster.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * NoteViewModel = "otak" aplikasi yang menyimpan dan mengelola semua catatan.
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NoteDatabase.getDatabase(application).dao

    // notes bersifat PUBLIC dan READ-ONLY — UI hanya boleh MEMBACA.
    // data mengalir turun dari Room (Database → UI).
    val notes: StateFlow<List<Note>> = dao.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /** Mencari catatan berdasarkan id — dipakai EditorScreen saat mode edit. */
    fun getNoteById(id: Long): Note? {
        return notes.value.find { it.id == id }
    }

    /**
     * Menyimpan catatan ke database Room.
     */
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

    /** Menghapus catatan. */
    fun deleteNote(id: Long) {
        viewModelScope.launch {
            dao.deleteNoteById(id)
        }
    }
}