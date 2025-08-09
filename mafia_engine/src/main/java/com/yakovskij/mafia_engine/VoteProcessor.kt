package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.VoteResult

class VoteProcessor(private var session: GameSession) {
    private val voteMap = mutableMapOf<Int, Int>() // voterId -> targetId
    private var voteResult: VoteResult? = null

    fun setSession(newSession: GameSession) {
        session = newSession
        clearVotes()
    }

    fun addVote(voterId: Int, targetId: Int) {
        voteMap[voterId] = targetId
    }

    fun clearVoteByPlayer(voterId: Int) {
        voteMap.remove(voterId)
    }

    fun clearVotes() {
        voteMap.clear()
        voteResult = null
    }

    fun getVoteResult(): VoteResult? = voteResult

    fun calculateVotesAndJail(): Int? {
        val groupedVotes = voteMap.values.groupingBy { it }.eachCount()
        val maxVotes = groupedVotes.maxByOrNull { it.value }?.value ?: return null
        val topTargets = groupedVotes.filter { it.value == maxVotes }.keys

        val players = session.state.players

        return if (topTargets.size == 1) {
            val chosenId = topTargets.first()
            val target = players.firstOrNull { it.id == chosenId }
            if (target != null) {
                voteResult = VoteResult.PlayerJailed(target)
                session.eliminatePlayer(chosenId)
            }
            voteMap.clear()
            chosenId
        } else {
            voteResult = VoteResult.Tie(
                players.filter { it.id in topTargets }
            )
            voteMap.clear()
            null
        }
    }

}
