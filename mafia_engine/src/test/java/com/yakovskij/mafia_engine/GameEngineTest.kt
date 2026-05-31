package com.yakovskij.mafiacards

import com.yakovskij.mafia_engine.*
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.domain.role.RoleType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameEngineTest {
    @Test
    fun `Победа после убийства жителя мафией на 3 игрока`() {
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

    @Test
    fun `getPlayerById возвращает игрока именно по id`() {
        val settings = GameSettings()
        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.CIVILIAN, 2)

        val players = listOf(
            Player(id = 10, name = "A"),
            Player(id = 20, name = "B"),
            Player(id = 30, name = "C")
        )
        val session = GameSession(settings)
        session.setupGame(players)

        assertEquals(20, session.getPlayerById(20)?.id)
        assertEquals(30, session.getPlayerById(30)?.id)
        assertEquals(null, session.getPlayerById(999))
    }

    @Test
    fun `мафия может действовать во вторую ночь после гибели первого игрока`() {
        val settings = GameSettings()
        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.DOCTOR, 1)
        settings.setCount(RoleType.CIVILIAN, 3)

        val players = (1..5).map { Player(id = it, name = "P$it") }
        val session = GameSession(settings)
        session.setupGame(players)
        val engine = GameEngine(session)
        engine.startGame()

        val mafia = session.state.players.first { it.role == RoleType.MAFIA }
        val victim = session.state.players.first { it.role == RoleType.CIVILIAN }

        // Первая ночь — мафия убивает мирного.
        engine.player(mafia.id).targets(victim.id)
        engine.performNightActions()
        assertFalse(session.state.players.first { it.id == victim.id }.isAlive)

        // Вторая ночь — мафия снова действует, ошибки быть не должно
        // (раньше getPlayerById возвращал первого игрока и кидал DeadPlayerAction).
        val secondVictim = session.state.players.first { it.isAlive && it.role == RoleType.CIVILIAN }
        engine.player(mafia.id).targets(secondVictim.id)
        val events = engine.performNightActions()
        assertTrue(events.isNotEmpty(), "Действие мафии во вторую ночь должно было сработать")
    }

    @Test
    fun `игра автоматически завершается победой мафии при равенстве`() {
        val settings = GameSettings()
        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.CIVILIAN, 2)

        val players = listOf(
            Player(id = 1, name = "A"),
            Player(id = 2, name = "B"),
            Player(id = 3, name = "C")
        )
        val session = GameSession(settings)
        session.setupGame(players)
        val engine = GameEngine(session)
        engine.startGame()

        engine.advancePhase() // SETUP -> DAY_WITHOUT_VOTING
        engine.advancePhase() // -> NIGHT

        val mafia = session.state.players.first { it.role == RoleType.MAFIA }
        val victim = session.state.players.first { it.role == RoleType.CIVILIAN }
        engine.player(mafia.id).targets(victim.id)
        engine.performNightActions()

        engine.advancePhase() // NIGHT -> NIGHT_ENDED
        assertEquals(GamePhase.NIGHT_ENDED, session.state.currentPhase)

        engine.advancePhase() // NIGHT_ENDED -> END (мафия = мирные)
        assertEquals(GamePhase.END, session.state.currentPhase)
        assertEquals(RoleType.MAFIA, session.state.winner)
    }

    @Test
    fun `мирные побеждают когда мафия мертва`() {
        val settings = GameSettings()
        settings.setCount(RoleType.MAFIA, 1)
        settings.setCount(RoleType.CIVILIAN, 2)

        val players = listOf(
            Player(id = 1, name = "A"),
            Player(id = 2, name = "B"),
            Player(id = 3, name = "C")
        )
        val session = GameSession(settings)
        session.setupGame(players)
        val engine = GameEngine(session)

        val mafia = session.state.players.first { it.role == RoleType.MAFIA }
        session.eliminatePlayer(mafia.id)

        assertEquals(RoleType.CIVILIAN, engine.checkWinCondition())
    }

    @Test
    fun `маньяк побеждает оставшись один на один`() {
        val settings = GameSettings()
        settings.setCount(RoleType.MANIAC, 1)
        settings.setCount(RoleType.CIVILIAN, 2)

        val players = listOf(
            Player(id = 1, name = "A"),
            Player(id = 2, name = "B"),
            Player(id = 3, name = "C")
        )
        val session = GameSession(settings)
        session.setupGame(players)
        val engine = GameEngine(session)

        val civilians = session.state.players.filter { it.role == RoleType.CIVILIAN }
        session.eliminatePlayer(civilians.first().id)

        assertEquals(RoleType.MANIAC, engine.checkWinCondition())
    }
}
