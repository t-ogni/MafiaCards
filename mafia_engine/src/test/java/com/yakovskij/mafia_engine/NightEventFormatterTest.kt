package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.NightEvent
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.RoleType
import com.yakovskij.mafia_engine.presentation.NightFormatter
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

class NightEventFormatterTest {
    private lateinit var nightFormatter: NightFormatter
    private lateinit var playerAlice: Player
    private lateinit var playerBob: Player
    private lateinit var playerCompose: Player
    private lateinit var playerDaniel: Player
    @Before
    fun setup() {
        nightFormatter = NightFormatter()
        playerAlice = Player(0, "Alice")
        playerBob = Player(1, "Bob")
        playerCompose = Player(2, "Compose")
        playerDaniel = Player(3, "Daniel")
    }

    @Test
    fun `formatter test result`() {

        playerAlice.role = RoleType.CIVILIAN
        playerBob.role = RoleType.CIVILIAN
        playerCompose.role = RoleType.DOCTOR
        playerDaniel.role = RoleType.MAFIA

        var events = listOf(
            NightEvent.KillAttempt(listOf(playerDaniel), playerDaniel.role!!, playerAlice, true),
            NightEvent.KillAttempt(listOf(playerDaniel), playerDaniel.role!!, playerAlice, false),
            NightEvent.DoctorSaved(playerCompose, playerAlice),
            NightEvent.DoctorSaved(playerCompose, playerBob),
            NightEvent.DetectiveChecked(playerAlice, playerBob),
            NightEvent.FailedAction(playerAlice, playerBob)
        )
        val result = nightFormatter.format(events)
        println()
        println(result)
        println()
        assertNotEquals(result, listOf(""))
    }
}