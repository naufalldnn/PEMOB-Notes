package com.naufal.mynote.model

data class Note(
    val id: Long = 0,
    val content: String,
    val color: Long = 0xFFFFF9C4,
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)