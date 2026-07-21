package com.naufal.mynote.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.naufal.mynote.navigation.Screen
import com.naufal.mynote.ui.theme.*
import com.naufal.mynote.viewmodel.NoteViewModel

@Composable
fun EditorScreen(
    viewModel: NoteViewModel,
    noteId: Long,
    onNavigateBack: () -> Unit
) {
    val isEditMode = noteId != Screen.Editor.NO_ID
    val existingNote = if (isEditMode) viewModel.getNoteById(noteId) else null

    EditorContent(
        isEditMode = isEditMode,
        existingContent = existingNote?.content.orEmpty(),
        existingColor = existingNote?.color ?: NoteIndigo.toArgb().toLong(),
        existingIsPinned = existingNote?.isPinned ?: false,
        onSave = { content, color, isPinned ->
            viewModel.saveNote(
                id = if (isEditMode) noteId else null,
                content = content,
                color = color,
                isPinned = isPinned
            )
            onNavigateBack()
        },
        onDelete = {
            if (isEditMode) {
                viewModel.deleteNote(noteId)
                onNavigateBack()
            }
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorContent(
    isEditMode: Boolean,
    existingContent: String,
    existingColor: Long,
    existingIsPinned: Boolean,
    onSave: (String, Long, Boolean) -> Unit,
    onDelete: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var textContent by rememberSaveable { mutableStateOf(existingContent) }
    var selectedColor by rememberSaveable { mutableStateOf(existingColor) }
    var isPinned by rememberSaveable { mutableStateOf(existingIsPinned) }

    val colors = listOf(
        NoteIndigo, NoteLavender, NoteDeepPurple, NoteBlue, NoteCyan, NoteWhite
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Edit Note" else "New Note",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                        }
                    }
                    IconButton(onClick = { isPinned = !isPinned }) {
                        Icon(
                            imageVector = if (isPinned) Icons.Rounded.PushPin else Icons.Outlined.PushPin,
                            contentDescription = "Pin"
                        )
                    }
                    IconButton(
                        onClick = { onSave(textContent, selectedColor, isPinned) },
                        enabled = textContent.isNotBlank()
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(selectedColor),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(selectedColor))
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(colors) { color ->
                    val colorArgb = color.toArgb().toLong()
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == colorArgb) 3.dp else 1.dp,
                                color = if (selectedColor == colorArgb) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .clickable { selectedColor = colorArgb }
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                TextField(
                    value = textContent,
                    onValueChange = { textContent = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    placeholder = { Text("Start typing your note...") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    MyNoteTheme {
        EditorContent(
            isEditMode = true,
            existingContent = "This is an existing note.",
            existingColor = NoteLavender.toArgb().toLong(),
            existingIsPinned = true,
            onSave = { _, _, _ -> },
            onDelete = {},
            onNavigateBack = {}
        )
    }
}
