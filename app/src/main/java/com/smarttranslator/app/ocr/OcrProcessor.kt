package com.smarttranslator.app.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OcrProcessor {
    suspend fun extractText(context: Context, uri: Uri): String = suspendCancellableCoroutine { continuation ->
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromFilePath(context, uri)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.textBlocks.joinToString("\n") { it.text }
                continuation.resume(text.ifBlank { "لم يتم استخراج نص من الصورة." })
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }
}
