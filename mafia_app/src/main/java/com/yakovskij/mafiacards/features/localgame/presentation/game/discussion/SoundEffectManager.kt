package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import android.content.Context
import android.media.SoundPool
import com.yakovskij.mafiacards.R

class SoundEffectManager(context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .build()

    private val typeSoundId: Int

    init {
        typeSoundId = soundPool.load(context, R.raw.typewriter, 1)
    }

    fun playKeySound() {
        soundPool.play(typeSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun stopKeySound() {
        soundPool.stop(typeSoundId)
    }

    fun release() {
        soundPool.release()
    }
}
