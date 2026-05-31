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

    // Возвращает роль-представителя победившей команды, либо null, если игра ещё идёт.
    // CIVILIAN -> победа мирных, MAFIA -> победа мафии, MANIAC -> победа маньяка.
    fun checkWinCondition(): RoleType? {
        val alive = session.getPlayers().filter { it.isAlive }
        val mafiaAlive = alive.count { it.role?.side == TeamSide.MAFIA }
        val maniacAlive = alive.count { it.role?.side == TeamSide.MANIACS }
        val nonMafiaAlive = alive.size - mafiaAlive

        return when {
            // Все опасные роли мертвы — побеждает город.
            mafiaAlive == 0 && maniacAlive == 0 -> RoleType.CIVILIAN
            // Маньяк остался один на один (или вовсе один) и мафии нет — побеждает маньяк.
            maniacAlive > 0 && mafiaAlive == 0 && alive.size <= 2 -> RoleType.MANIAC
            // Мафия сравнялась/превзошла остальных и одиночек-маньяков нет — побеждает мафия.
            mafiaAlive > 0 && maniacAlive == 0 && mafiaAlive >= nonMafiaAlive -> RoleType.MAFIA
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
        val currentPhase = session.state.currentPhase
        onOldPhaseEnded(currentPhase)

        // После просмотра газеты (NIGHT_ENDED) и итогов голосования (VOTING_ENDED)
        // проверяем, не закончилась ли игра.
        if (currentPhase == GamePhase.NIGHT_ENDED || currentPhase == GamePhase.VOTING_ENDED) {
            val winner = checkWinCondition()
            if (winner != null) {
                endGame(winner)
                return
            }
        }

        val nextPhase = getNextPhase()
        session.setPhase(nextPhase)
        onNewPhase(nextPhase)
    }


    private fun onOldPhaseEnded(phase: GamePhase) {
        when (phase) {
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
