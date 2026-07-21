package com.syaiful.mynotemaster.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Note adalah "cetakan" (blueprint) satu catatan.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,             // autoGenerate = true agar Room mengurus ID
    val content: String,
    val color: Long = 0xFFFFF9C4, // Default Kuning Muda
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)