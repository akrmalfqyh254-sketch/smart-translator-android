package com.smarttranslator.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smarttranslator.app.ocr.OcrProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController: NavController) {
    var recognizedText by remember { mutableStateOf("استخراج النص من الصورة باستخدام OCR سيظهر هنا.") }
    var isProcessing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                scope.launch {
                    isProcessing = true
                    recognizedText = "جارٍ استخراج النص..."
                    val result = withContext(Dispatchers.IO) {
                        try {
                            OcrProcessor().extractText(context, uri)
                        } catch (e: Exception) {
                            "تعذر استخراج النص: ${e.localizedMessage ?: "خطأ غير معروف"}"
                        }
                    }
                    recognizedText = result
                    isProcessing = false
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ترجمة الصور / OCR") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("التقاط صورة أو اختيار صورة من المعرض")
                    Text("سيتم استخراج النص عبر ML Kit OCR ثم ترجمته.")
                    Text(recognizedText)
                    Button(onClick = { /* camera flow placeholder */ }, enabled = !isProcessing) {
                        Text(if (isProcessing) "جارٍ..." else "فتح الكاميرا")
                    }
                    Button(
                        onClick = {
                            pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        enabled = !isProcessing
                    ) {
                        Text("اختيار صورة")
                    }
                }
            }
        }
    }
}
