package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.Player

class PlayerController(private val performerId: Int, private val engine: GameEngine) {
    fun voted(targetId: Int?): PlayerController {
        if (targetId != null) {
            engine.voteProcessor.addVote(performerId, targetId)
        }
        return this
    }

    fun voted(target: Player?): PlayerController {
        if (target != null) {
            engine.voteProcessor.addVote(performerId, target.id)
        }
        return this
    }

    fun targets(targetId: Int?): PlayerController {
        if (targetId != null) {
            engine.nightProcessor.addAction(performerId, targetId)
        }
        return this
    }

    fun targets(target: Player?): PlayerController {
        if (target != null) {
            engine.nightProcessor.addAction(performerId, target.id)
        }
        return this
    }
    fun clearVote(){
        engine.voteProcessor.clearVoteByPlayer(performerId)
    }
}

