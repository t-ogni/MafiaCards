package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.*


// GameSession — это хранилище и управляющий объект состояния игры
// Выполняет чисто мутации состояния

class GameSession(
    private val settings: GameSettings
) {
    private val _state = GameState(
        players = emptyList(),
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
        _state.turnNumber = 0
    }

    fun setPhase(next: GamePhase) {
        _state.currentPhase = next
    }

    fun setWinner(winner: RoleType) {
        _state.winner = winner
    }

    private fun generateDeck(): List<RoleType> {
        val deck = mutableListOf<RoleType>()

        // Добавляем роли
        repeat(settings.mafiaCount) { deck.add(RoleType.MAFIA) }
        repeat(settings.doctorCount) { deck.add(RoleType.DOCTOR) }
        repeat(settings.detectiveCount) { deck.add(RoleType.DETECTIVE) }

        while (deck.size < settings.totalPlayers) {
            deck.add(RoleType.CIVILIAN)
        }

        return deck
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
}
