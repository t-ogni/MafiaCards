package com.yakovskij.mafiacards.features.game.data

import com.yakovskij.mafiacards.features.game.domain.GamePhase
import com.yakovskij.mafiacards.features.game.domain.NightAction
import com.yakovskij.mafiacards.features.game.domain.RoleType

//  GameEngine — это мозг игры, её логика, сценарии, искусственный режиссёр
//  принимает решения,
//  оркестрирует действия,
//  реагирует на фазы и события
//  реализует сложную логику, вроде «мафия голосует за убийство», «док лечит», «детектив проверяет»
//

class GameEngine(
    private var session: GameSession,
    private val voteProcessor: VoteProcessor = VoteProcessor(session),
    private val nightProcessor: NightProcessor = NightProcessor(session)
) {
    fun getSession(): GameSession {
        return session
    }
    fun setSession(newSession: GameSession) {
        this.session = newSession
        voteProcessor.setSession(newSession)
        nightProcessor.setSession(newSession)
    }

    fun user(id: Int): PlayerController {
        return PlayerController(id, this)
    }

    fun calculateVotesAndJail(): Int? = voteProcessor.calculateVotesAndJail()
    fun performNightActions() = nightProcessor.performNightActions()

    fun checkWinCondition(): RoleType? {
        val mafiaAlive = session.state.players.count { it.role == RoleType.MAFIA && it.isAlive }
        val civiliansAlive = session.state.players.count { it.role != RoleType.MAFIA && it.isAlive }

        return when {
            mafiaAlive == 0 -> RoleType.CIVILIAN
            civiliansAlive == 1 && mafiaAlive > 0 -> RoleType.MAFIA
            else -> null
        }
    }

    fun startGame() {
        session.setPhase(GamePhase.SETUP)
    }

    fun endGame(winner : RoleType = RoleType.MAFIA) {
        session.setPhase(GamePhase.END)
        session.setWinner(winner)
    }

    fun advancePhase() {
        val nextPhase = when (session.state.currentPhase) {
            GamePhase.SETUP -> GamePhase.NIGHT
            GamePhase.NIGHT -> GamePhase.DAY_DISCUSSION
            GamePhase.DAY_DISCUSSION -> GamePhase.VOTING
            GamePhase.VOTING -> GamePhase.VOTED
            GamePhase.VOTED -> GamePhase.NIGHT
            GamePhase.END -> GamePhase.END
        }

        session.setPhase(nextPhase)
        onPhaseChange(nextPhase)
    }

    private fun onPhaseChange(phase: GamePhase) {
        when (phase) {
            GamePhase.NIGHT -> {
                voteProcessor.reset()          // очищаем голоса
                nightProcessor.clearActions()  // начинаем ночь
            }
            GamePhase.DAY_DISCUSSION -> {
                nightProcessor.clearActions()
            }
            GamePhase.VOTING -> {
                voteProcessor.reset()
            }
            else -> {} // setup, end — ничего не делаем
        }

        // Можно добавить лог:
        // gameLog.add(PhaseChangedEvent(phase))
    }

    fun getNightResults(): List<NightAction> = nightProcessor.getLastNightResults()


    inner class PlayerController(private val performerId: Int, private val engine: GameEngine) {
        fun voted(targetId: Int?): PlayerController {
            if (targetId != null) {
                engine.voteProcessor.addVote(performerId, targetId)
            }
            return this
        }

        fun targets(targetId: Int?): PlayerController {
            if (targetId != null) {
                engine.nightProcessor.addAction(performerId, targetId)
            }
            return this
        }
    }
}
