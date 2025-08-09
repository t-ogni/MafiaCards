package com.yakovskij.mafiacards

import com.yakovskij.mafia_engine.domain.GameException
import com.yakovskij.mafia_engine.domain.GamePhase
import com.yakovskij.mafia_engine.domain.GameSettings
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.role.RoleType
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {

    private lateinit var repository: GameRepository
    private lateinit var settings: GameSettings
    private lateinit var players: List<Player>

    @Before
    fun setup() {
        repository = GameRepository()
        settings = GameSettings(
            totalPlayers = 4,
            mafiaCount = 1,
            doctorCount = 1,
            detectiveCount = 1
        )
        players = listOf(
            Player(1, "A"),
            Player(2, "B"),
            Player(3, "C"),
            Player(4, "D")
        )
    }

    // 1. Сохраняются настройки и можно их использовать
    @Test
    fun `saveSettings stores configuration`() {
        repository.saveSettings(settings)
        // Проверяется косвенно через startGame()
        repository.startGame(players)
        val state = repository.getState()
        assertNotNull(state)
        assertEquals(GamePhase.SETUP, state.currentPhase)
        assertEquals(4, state.players.size)
    }

    //️ 2. Без settings не запускается
    @Test(expected = GameException.GameNotSetup::class)
    fun `startGame without settings throws GameNotSetup`() {
        repository.startGame(players) // SETUP

    }

    // 3. newSession создаёт новую сессию
    @Test
    fun `newSession replaces current session`() {
        repository.saveSettings(settings)

        repository.startGame(players)
        repository.getEngine().endGame(RoleType.CIVILIAN)

        val oldState = repository.getState()
        val oldPhase = oldState.currentPhase

        assertEquals(GamePhase.END, oldPhase)
        assertEquals(RoleType.CIVILIAN, oldState.winner)

        repository.newSession()

        val newState = repository.getState()
        val newPhase = newState.currentPhase

        // Ожидаем, что новый объект GameSession создан (сброшен)
        assertNotSame(oldState, newState)
        assertEquals(GamePhase.SETUP, newPhase) // default phase после init
        assertNull(newState.winner)
    }
}