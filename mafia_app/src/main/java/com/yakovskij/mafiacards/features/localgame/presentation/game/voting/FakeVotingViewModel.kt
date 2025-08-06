package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.*
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.FakeGameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository


class FakeVotingViewModel : VotingViewModel(
    gameRepository = GameRepository(),
    settingsRepository = FakeGameSettingsRepository()
) {
    private val _fakeUiState = mutableStateOf(
        VotingUiState(
            shouldInit = false,
            votingCandidates = listOf(
                Player(id = 1, name = "Анна", isAlive = true),
                Player(id = 2, name = "Борис", isAlive = true),
                Player(id = 3, name = "Борис2", isAlive = true),
                Player(id = 4, name = "Борис3", isAlive = true),
                Player(id = 5, name = "Борис4", isAlive = true),
                Player(id = 6, name = "Борис5", isAlive = true),
                Player(id = 7, name = "БорисМертв", isAlive = false),
                Player(id = 8, name = "Борис7", isAlive = true),
                Player(id = 8, name = "Борис7", isAlive = true),
                Player(id = 8, name = "Борис7", isAlive = true),
                Player(id = 8, name = "Борис7", isAlive = true),
                Player(id = 8, name = "Борис7", isAlive = true),
                Player(id = 8, name = "Борис7", isAlive = true),
            ),
            currentPlayer = Player(id = 99, name = "ИгрокИмя", isAlive = true),
            selectedToVotePlayer = null,
            isVotingFinished = true
        )
    )

    override val uiState: State<VotingUiState>
        get() = _fakeUiState

    override fun addVote(performer: Player, target: Player) {
        println("Fake: ${performer.name} голосует за ${target.name}")
    }

    override fun initState() {
        // Не нужен для превью
    }
}

