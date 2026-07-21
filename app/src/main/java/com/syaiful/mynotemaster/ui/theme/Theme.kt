package com.syaiful.mynotemaster.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource

import com.syaiful.mynotemaster.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
/**
 * MyNoteTheme membungkus seluruh UI aplikasi.
 *
 * MENGAPA warna dibaca via colorResource()?
 * Karena sumber kebenaran (single source of truth) warna ada di colors.xml.
 * Jika desainer ingin mengganti palet, cukup ubah XML — tidak perlu
 * menyentuh kode Kotlin sama sekali.
 */
@Composable
fun MyNoteTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = colorResource(id = R.color.note_primary),
        onPrimary = colorResource(id = R.color.note_on_primary),
        surface = colorResource(id = R.color.note_surface),
        onSurface = colorResource(id = R.color.note_on_surface),
        background = colorResource(id = R.color.note_background),
        secondaryContainer = colorResource(id = R.color.note_secondary_container),
        onSecondaryContainer = colorResource(id = R.color.note_on_secondary_container)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}