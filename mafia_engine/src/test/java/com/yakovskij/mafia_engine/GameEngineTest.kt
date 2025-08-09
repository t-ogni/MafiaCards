package com.yakovskij.mafiacards

import com.yakovskij.mafia_engine.*
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.domain.role.RoleType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameEngineTest {
    @Test
    fun `Победа после убийства жителя мафией на 3 игрока`() {
        val settings = GameSettings()

        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.DOCTOR, 1)
        settings.setCount(RoleType.CIVILIAN, 2)

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

        engine.player(mafia.id).targets(civ.first().id)
        engine.performNightActions()

        val result = engine.checkWinCondition() == RoleType.MAFIA
        assertTrue(result, "Гражданских остаётся 1 человек — должна победить мафия")

        engine.endGame(RoleType.MAFIA)
        assertEquals(session.state.winner, RoleType.MAFIA, "Победа мафии в данной сессии")
    }

    @Test
    fun `док спасает жителя от убийства`() {
        val settings = GameSettings()

        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.DOCTOR, 1)
        settings.setCount(RoleType.CIVILIAN, 1)

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

        engine.player(mafia.id).targets(civ.id)
        engine.player(doc.id).targets(civ.id)

        engine.performNightActions()

        val civAfterAction = session.state.players.firstOrNull { it.id == civ.id }!!
        assertTrue(civAfterAction.isAlive, "Житель не живой. должен был выжить")
    }

    @Test
    fun `Кик на голосовании`() {
        val settings = GameSettings()

        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.DOCTOR, 1)
        settings.setCount(RoleType.CIVILIAN, 1)

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

        engine.player(mafia.id).voted(doc.id)
        engine.player(civ.id).voted(doc.id)
        engine.player(doc.id).voted(mafia.id)

        engine.calculateVotesAndJail()

        val docAfterAction = session.state.players.firstOrNull { it.id == doc.id }!!
        assertTrue(!docAfterAction.isAlive, "Житель живой. должен был быть в тюрьме при голосовании")
    }
}
