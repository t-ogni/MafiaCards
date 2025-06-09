package com.yakovskij.mafiacards.features.game.data

class VoteProcessor(private var session: GameSession) {
    private val voteMap = mutableMapOf<Int, Int>() // voterId -> targetId

    fun setSession(newSession: GameSession) {
        session = newSession
        voteMap.clear()
    }

    fun addVote(voterId: Int, targetId: Int) {
        voteMap[voterId] = targetId
    }

    fun reset() {
        voteMap.clear()
    }

    fun calculateVotesAndJail(): Int? {
        val groupedVotes = voteMap.values.groupingBy { it }.eachCount()
        val maxVotes = groupedVotes.maxByOrNull { it.value }?.value ?: return null
        val topTargets = groupedVotes.filter { it.value == maxVotes }.keys

        return if (topTargets.size == 1) {
            val chosen = topTargets.first()
            session.eliminatePlayer(chosen)
            voteMap.clear()
            chosen
        } else {
            voteMap.clear()
            null // Ничья — никто не умирает
        }
    }
}
