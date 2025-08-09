package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.domain.role.RoleType


// GameSession — это хранилище и управляющий объект состояния игры
// Выполняет чисто мутации состояния

class GameSession(
    private val settings: GameSettings
) {
    private val _state = GameState(
        players = emptyList(),
        currentPhase = GamePhase.SETUP,
        cycleNumber = 0,
        winner = null
    )
    val state: GameState get() = _state.copy()

    fun setupGame(players: List<Player>) {
        val deck = settings.allRolesList().shuffled()

        if (players.size != deck.size) {
            throw GameException.PlayerCountMismatch(deck.size, players.size)
        }

        val uniqueIds = players.map { it.id }.toSet()
        if (uniqueIds.size != players.size) {
            val duplicateId = players.groupingBy { it.id }.eachCount().filter { it.value > 1 }.keys.first()
            throw GameException.DuplicatePlayerId(duplicateId)
        }

        val assignedPlayers = players.mapIndexed { index, player ->
            player.copy(role = deck[index])
        }

        _state.players = assignedPlayers
        _state.winner = null
        _state.currentPhase = GamePhase.SETUP
        _state.cycleNumber = 0
    }

    fun setPhase(next: GamePhase) {
        _state.currentPhase = next
    }

    fun setWinner(winner: RoleType) {
        _state.winner = winner
    }

    fun eliminatePlayer(playerId: Int) {
        val updated = _state.players.map {
            if (it.id == playerId) it.copy(isAlive = false) else it
        }
        _state.players = updated
    }

    fun revivalPlayer(playerId: Int) {
        val updated = _state.players.map {
            if (it.id == playerId) it.copy(isAlive = true) else it
        }
        _state.players = updated
    }

    fun getPlayerById(playerId: Int): Player? {
        return _state.players.firstOrNull()
    }

    fun getPlayers(): List<Player> {
        return _state.players
    }

    fun incrementCycle() {
        _state.cycleNumber += 1
    }
}
