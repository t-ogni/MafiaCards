package com.yakovskij.mafiacards.features.game.data

import com.yakovskij.mafiacards.features.game.domain.GamePhase
import com.yakovskij.mafiacards.features.game.domain.GameSettings
import com.yakovskij.mafiacards.features.game.domain.GameException
import com.yakovskij.mafiacards.features.game.domain.GameState
import com.yakovskij.mafiacards.features.game.domain.NightAction
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.game.domain.RoleCard
import com.yakovskij.mafiacards.features.game.domain.RoleType


// GameSession — это хранилище и управляющий объект состояния игры
// Выполняет чисто мутации состояния

class GameSession(
    private val settings: GameSettings
) {
    private val _state = GameState(
        players = emptyList(),
        deck = emptyList(),
        currentPhase = GamePhase.SETUP,
        turnNumber = 0,
        winner = null
    )
    val state: GameState get() = _state.copy()

    fun setupGame(players: List<Player>) {
        val deck = generateDeck().shuffled()

        if (players.size != deck.size) {
            throw GameException.PlayerCountMismatch(deck.size, players.size)
        }

        val uniqueIds = players.map { it.id }.toSet()
        if (uniqueIds.size != players.size) {
            val duplicateId = players.groupingBy { it.id }.eachCount().filter { it.value > 1 }.keys.first()
            throw GameException.DuplicatePlayerId(duplicateId)
        }

        if (settings.mafiaCount < 0 || settings.doctorCount < 0 || settings.detectiveCount < 0) {
            throw GameException.InvalidRoleCount("mafia/doctor/detective", -1) // Здесь можно улучшить с детализацией
        }

        val assignedPlayers = players.mapIndexed { index, player ->
            player.copy(role = deck[index])
        }

        _state.players = assignedPlayers
        _state.winner = null
        _state.currentPhase = GamePhase.SETUP
        _state.deck = deck
        _state.turnNumber = 0
    }

    fun setPhase(next: GamePhase) {
        _state.currentPhase = next
    }

    fun setWinner(winner: RoleType) {
        _state.winner = winner
    }

    private fun generateDeck(): List<RoleCard> {
        val deck = mutableListOf<RoleCard>()

        // Добавляем роли
        repeat(settings.mafiaCount) { deck.add(RoleCard(RoleType.MAFIA)) }
        repeat(settings.doctorCount) { deck.add(RoleCard(RoleType.DOCTOR)) }
        repeat(settings.detectiveCount) { deck.add(RoleCard(RoleType.DETECTIVE)) }

        while (deck.size < settings.totalPlayers) {
            deck.add(RoleCard(RoleType.CIVILIAN))
        }

        return deck
    }

    fun eliminatePlayer(playerId: Int) {
        val updated = _state.players.map {
            if (it.id == playerId) it.copy(isAlive = false) else it
        }
        _state.players = updated
    }
}
