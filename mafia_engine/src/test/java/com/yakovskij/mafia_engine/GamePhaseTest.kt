package com.yakovskij.mafiacards

import com.yakovskij.mafia_engine.*
import com.yakovskij.mafia_engine.domain.*

import org.junit.Before
import org.junit.Test
import kotlin.test.*

class GamePhaseTest {

    private lateinit var settings: GameSettings
    private lateinit var session: GameSession
    private lateinit var engine: GameEngine
    private lateinit var voteProcessor: VoteProcessor
    private lateinit var nightProcessor: NightProcessor

    @Before
    fun setup() {
        settings = GameSettings(
            totalPlayers = 4,
            mafiaCount = 1,
            doctorCount = 1,
            detectiveCount = 1
        )

        session = GameSession(settings)
        voteProcessor = VoteProcessor(session)
        nightProcessor = NightProcessor(session)
        engine = GameEngine(session, voteProcessor, nightProcessor)

        val players = listOf(
            Player(id = 1, name = "A"),
            Player(id = 2, name = "B"),
            Player(id = 3, name = "C"),
            Player(id = 4, name = "D")
        )

        session.setupGame(players)
        engine.startGame()
    }
    @Test
    fun `advancePhase should cycle through game phases`() {
        assertEquals(GamePhase.NIGHT, session.state.currentPhase)

        engine.advancePhase() // NIGHT → DAY_DISCUSSION
        assertEquals(GamePhase.DAY_DISCUSSION, session.state.currentPhase)

        engine.advancePhase() // DAY_DISCUSSION → VOTING
        assertEquals(GamePhase.VOTING, session.state.currentPhase)

        engine.advancePhase() // VOTING → VOTED
        assertEquals(GamePhase.VOTED, session.state.currentPhase)

        engine.advancePhase() // VOTED → NIGHT
        assertEquals(GamePhase.NIGHT, session.state.currentPhase)
    }

    @Test
    fun `transition to NIGHT clears votes and night actions`() {
        // Добавим голос
        engine.user(1).voted(2)
        assertNotNull(voteProcessor.calculateVotesAndJail()) // проголосовал

        // Добавим ночное действие (допустим, от DOCTOR)
        engine.user(3).targets(1)
        assertFalse(nightProcessor.getActions().isEmpty())

        engine.advancePhase() // NIGHT → DAY_DISCUSSION
        engine.advancePhase() // DAY_DISCUSSION → VOTING
        engine.advancePhase() // VOTING → VOTED
        engine.advancePhase() // VOTED → NIGHT

        // После перехода в NIGHT должно быть всё сброшено
        assertTrue(voteProcessor.calculateVotesAndJail() == null)
        assertTrue(nightProcessor.getActions().isEmpty())
    }

    @Test
    fun `endGame sets phase to END`() {
        engine.endGame(RoleType.MAFIA)
        assertEquals(GamePhase.END, session.state.currentPhase)
        assertEquals(RoleType.MAFIA, session.state.winner)
    }
}
