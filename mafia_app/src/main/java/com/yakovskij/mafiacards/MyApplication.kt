package com.yakovskij.mafiacards

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application()

val Context.gameSettingsDataStore by preferencesDataStore(name = "game_settings")