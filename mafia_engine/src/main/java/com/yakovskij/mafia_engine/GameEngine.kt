package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.*

//  GameEngine — это мозг игры, её логика, сценарии, искусственный режиссёр
//  принимает решения,
//  оркестрирует действия,
//  реагирует на фазы и события
//  реализует сложную логику, вроде «мафия голосует за убийство», «док лечит», «детектив проверяет»
//

open class GameEngine(
    internal var session: GameSession,
    internal val voteProcessor: VoteProcessor = VoteProcessor(session),
    internal val nightProcessor: NightProcessor = NightProcessor(session)
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

    fun user(player: Player): PlayerController {
        return PlayerController(player.id, this)
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

    fun getNextPhase(): GamePhase {
        val nextPhase = when (session.state.currentPhase) {
            GamePhase.SETUP -> GamePhase.DAY_WITHOUT_VOTING
            GamePhase.DAY_WITHOUT_VOTING -> GamePhase.NIGHT
            GamePhase.NIGHT -> GamePhase.NIGHT_ENDED
            GamePhase.NIGHT_ENDED -> GamePhase.DAY_DISCUSSION
            GamePhase.DAY_DISCUSSION -> GamePhase.VOTING
            GamePhase.VOTING -> GamePhase.VOTING_ENDED
            GamePhase.VOTING_ENDED -> GamePhase.NIGHT
            GamePhase.END -> GamePhase.END
        }
        return nextPhase
    }
    fun advancePhase() {
        val nextPhase = getNextPhase()
        session.setPhase(nextPhase)
        onPhaseChange(nextPhase)
    }

    private fun onPhaseChange(phase: GamePhase) {
        when (phase) {
            GamePhase.NIGHT -> {
                nightProcessor.clearEvents()
                nightProcessor.clearActions()  // начинаем ночь
            }
            GamePhase.VOTING -> {
                voteProcessor.clearVotes()
            }

            else -> {}
        }
        checkWinCondition()
    }

    fun getNightResults(): List<NightEvent> = nightProcessor.getEvents()
    fun getVoteResults(): VoteResult? = voteProcessor.getVoteResult()
}
