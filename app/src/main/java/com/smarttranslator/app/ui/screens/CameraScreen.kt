package com.smarttranslator.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smarttranslator.app.model.Language
import com.smarttranslator.app.model.SupportedLanguages
import com.smarttranslator.app.translation.TranslationEngine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var sourceText by remember {
        mutableStateOf("مرحبا بالعالم، أريد ترجمة هذا النص إلى الإنجليزية بسرعة وبشكل دقيق.")
    }
    var sourceLanguage by remember { mutableStateOf(SupportedLanguages.list.first { it.code == "ar" }) }
    var targetLanguage by remember { mutableStateOf(SupportedLanguages.list.first { it.code == "en" }) }
    var translatedText by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }

    val engine = remember { TranslationEngine() }
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("المترجم الذكي") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "الإعدادات")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LanguagePill(language = sourceLanguage) {
                            val index = SupportedLanguages.list.indexOf(sourceLanguage)
                            val nextIndex = if (index >= SupportedLanguages.list.lastIndex) 0 else index + 1
                            sourceLanguage = SupportedLanguages.list[nextIndex]
                        }
                        IconButton(onClick = {
                            val temp = sourceLanguage
                            sourceLanguage = targetLanguage
                            targetLanguage = temp
                        }) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = "تبديل اللغات")
                        }
                        LanguagePill(language = targetLanguage) {
                            val index = SupportedLanguages.list.indexOf(targetLanguage)
                            val nextIndex = if (index >= SupportedLanguages.list.lastIndex) 0 else index + 1
                            targetLanguage = SupportedLanguages.list[nextIndex]
                        }
                    }

                    OutlinedTextField(
                        value = sourceText,
                        onValueChange = { sourceText = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        label = { Text("النص") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = { /* speech recognition hook */ }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.MicNone, contentDescription = null)
                            Text("صوت")
                        }
                        Button(
                            onClick = {
                                isTranslating = true
                                scope.launch {
                                    translatedText = engine.translateText(sourceText, sourceLanguage, targetLanguage)
                                    isTranslating = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isTranslating
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Text(if (isTranslating) "جارٍ..." else "ترجمة")
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("الترجمة", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            if (translatedText.isNotBlank()) {
                                clipboard.setText(AnnotatedString(translatedText))
                            }
                        }) {
                            Icon(Icons.Default.CopyAll, contentDescription = "نسخ")
                        }
                    }
                    Text(
                        if (translatedText.isBlank()) "سيظهر النص المترجم هنا." else translatedText
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { navController.navigate("history") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Text("السجل")
                }
                Button(onClick = { navController.navigate("favorites") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    Text("المفضلة")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { navController.navigate("camera") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Text("OCR")
                }
                Button(onClick = { /* floating overlay */ }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Mic, contentDescription = null)
                    Text("عائمة")
                }
            }
        }
    }
}

@Composable
private fun LanguagePill(language: Language, onLanguageSelected: (Language) -> Unit) {
    Button(onClick = { onLanguageSelected(language) }) {
        Text(language.nativeLabel)
    }
}
