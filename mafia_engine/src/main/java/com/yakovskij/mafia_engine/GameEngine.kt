package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.domain.role.RoleType
import com.yakovskij.mafia_engine.domain.role.TeamSide

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

    fun getPlayers(): List<Player> {
        return session.getPlayers()
    }

    fun player(id: Int): PlayerController {
        return PlayerController(id, this)
    }

    fun player(player: Player): PlayerController {
        return PlayerController(player.id, this)
    }
    fun calculateVotesAndJail(): Int? = voteProcessor.calculateVotesAndJail()
    fun performNightActions() = nightProcessor.performNightActions()

    fun checkWinCondition(): RoleType? {
        val players = session.getPlayers()
        val mafiaAlive = players.count { it.isAlive && it.role?.side == TeamSide.MAFIA }
        val civAlive = players.count { it.isAlive && it.role?.side == TeamSide.CIVILIANS }
        return when {
            mafiaAlive == 0 -> RoleType.CIVILIAN
            civAlive == 1 && mafiaAlive > 0 -> RoleType.MAFIA
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
        onOldPhaseEnded(session.state.currentPhase)
        val nextPhase = getNextPhase()
        session.setPhase(nextPhase)
        onNewPhase(nextPhase)
    }


    private fun onOldPhaseEnded(phase: GamePhase) {
        when (phase) {
            GamePhase.NIGHT_ENDED -> {
                checkWinCondition()  // после просмотра газеты посмотрим, есть ли победа
            }
            GamePhase.VOTING_ENDED -> {
                checkWinCondition()  // после тюрьмы посмотрим, есть ли победа
            }
            else -> {}
        }
    }

    private fun onNewPhase(phase: GamePhase) {
        when (phase) {
            GamePhase.NIGHT -> {
                nightProcessor.clearEvents()
                nightProcessor.clearActions()  // начинаем ночь
            }
            GamePhase.VOTING -> {
                voteProcessor.clearVotes()
            }
            GamePhase.DAY_WITHOUT_VOTING -> {
                session.incrementCycle()
            }
            GamePhase.DAY_DISCUSSION -> {
                session.incrementCycle()
            }

            else -> {}
        }
    }

    fun getNightResults(): List<NightEvent> = nightProcessor.getEvents()
    fun getVoteResults(): VoteResult? = voteProcessor.getVoteResult()
}
