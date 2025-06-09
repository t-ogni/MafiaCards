package com.yakovskij.mafiacards.features.localgame.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameTimerJob @Inject constructor() {
    private val _remainingTime = MutableStateFlow(0)
    val remainingTimeFlow: StateFlow<Int> = _remainingTime

    private var timerJob: Job? = null

    fun start(durationSec: Int, onFinished: () -> Unit) {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            for (i in durationSec downTo 0) {
                _remainingTime.emit(i)
                delay(1000)
            }
            onFinished()
        }
    }

    fun stop() {
        timerJob?.cancel()
    }
}
