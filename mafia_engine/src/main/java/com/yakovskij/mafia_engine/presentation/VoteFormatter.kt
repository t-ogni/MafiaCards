package com.yakovskij.mafia_engine.presentation

import com.yakovskij.mafia_engine.domain.VoteResult

class VoteFormatter {
    fun format(voteResult: VoteResult?): String {
        when(voteResult){
            is VoteResult.PlayerJailed -> {
                val playerName = voteResult.player.name
                return "Игрок $playerName оказался в тюрьме!"
            }

            is VoteResult.Tie -> {
                val playerNames = if (voteResult.tiedPlayers.size == 2) {
                    "${voteResult.tiedPlayers[0].name} и ${voteResult.tiedPlayers[1].name}"
                } else {
                    voteResult.tiedPlayers.joinToString(", ") { it.name }
                }
                return "Город не смог придти к единому мнению, голоса разделились между игроками $playerNames."
            }

            null -> {

                return "Голосование не проводилось."
            }
        }
    }
}