package com.yakovskij.mafiacards

import com.yakovskij.mafiacards.features.game.data.GameEngine
import com.yakovskij.mafiacards.features.game.data.GameSession
import com.yakovskij.mafiacards.features.game.domain.GameSettings
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.game.domain.RoleType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameEngineTest {
    @Test
    fun `Победа после убийства жителя мафией на 3 игрока`() {
        val settings = GameSettings(3, 1, 0, 0)

        val players = listOf(
            Player(id = 1, name = "Андрей"),
            Player(id = 2, name = "Костя"),
            Player(id = 3, name = "Маша")
        )

        val session = GameSession(settings)

        session.setupGame(players)
        val engine = GameEngine(session)

        engine.startGame()

        val mafia = session.state.players.find { it.role == RoleType.MAFIA }!!
        val civ = session.state.players.filter { it.role == RoleType.CIVILIAN }

        engine.user(mafia.id).targets(civ.first().id)
        engine.performNightActions()

        val result = engine.checkWinCondition() == RoleType.MAFIA
        assertTrue(result, "Гражданских остаётся 1 человек — должна победить мафия")

        engine.endGame(RoleType.MAFIA)
        assertEquals(session.state.winner, RoleType.MAFIA, "Победа мафии в данной сессии")
    }

    @Test
    fun `док спасает жителя от убийства`() {
        val settings = GameSettings(3, 1, 1, 0)

        val players = listOf(
            Player(id = 1, name = "Андрей"),
            Player(id = 2, name = "Костя"),
            Player(id = 3, name = "Маша")
        )

        val session = GameSession(settings)
        session.setupGame(players)

        val engine = GameEngine(session)

        engine.startGame()

        val mafia = session.state.players.firstOrNull { it.role == RoleType.MAFIA }!!
        val doc = session.state.players.firstOrNull { it.role == RoleType.DOCTOR }!!
        val civ = session.state.players.firstOrNull { it.role == RoleType.CIVILIAN }!!

        engine.user(mafia.id).targets(civ.id)
        engine.user(doc.id).targets(civ.id)

        engine.performNightActions()

        val civAfterAction = session.state.players.firstOrNull { it.id == civ.id }!!
        assertTrue(civAfterAction.isAlive, "Житель не живой. должен был выжить")
    }
    @Test
    fun `Кик на голосовании`() {
        val settings = GameSettings(3, 1, 1, 0)

        val players = listOf(
            Player(id = 1, name = "Андрей"),
            Player(id = 2, name = "Костя"),
            Player(id = 3, name = "Маша")
        )

        val session = GameSession(settings)
        session.setupGame(players)

        val engine = GameEngine(session)

        engine.startGame()

        engine.performNightActions()

        val mafia = session.state.players.firstOrNull { it.role == RoleType.MAFIA }!!
        val doc = session.state.players.firstOrNull { it.role == RoleType.DOCTOR }!!
        val civ = session.state.players.firstOrNull { it.role == RoleType.CIVILIAN }!!

        engine.user(mafia.id).voted(doc.id)
        engine.user(civ.id).voted(doc.id)
        engine.user(doc.id).voted(mafia.id)

        engine.calculateVotesAndJail()

        val docAfterAction = session.state.players.firstOrNull { it.id == doc.id }!!
        assertTrue(!docAfterAction.isAlive, "Житель живой. должен был быть в тюрьме при голосовании")
    }
}
