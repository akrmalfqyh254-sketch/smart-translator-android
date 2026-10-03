package com.smarttranslator.app

import android.app.Application
import com.smarttranslator.app.data.SmartTranslatorDatabase

class SmartTranslatorApplication : Application() {
    val database by lazy { SmartTranslatorDatabase.getDatabase(this) }
}
