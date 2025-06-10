package com.yakovskij.mafiacards.features.game.data

import com.yakovskij.mafiacards.features.game.domain.GameException
import com.yakovskij.mafiacards.features.game.domain.GameSettings
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.game.domain.RoleType
import javax.inject.Inject
import javax.inject.Singleton


// Это строго по принципам чистой архитектуры (Clean Architecture):
//  GameSession — уровень Entity. Он содержит чистые данные и их изменения.
//  GameEngine — уровень UseCase / Interactor. Содержит бизнес-логику, "мозг".
//  GameRepository — мост между слоями, предоставляет доступ и контролирует жизненный цикл.

@Singleton
class GameRepository @Inject constructor() {

    private var session: GameSession? = null
    var engine: GameEngine? = null
        private set
    private var settings: GameSettings? = null

    fun saveSettings(settings: GameSettings){
        this.settings = settings
    }


    fun startGame(players: List<Player>, settings: GameSettings? = null) {
        if(settings != null)
            this.settings = settings

        if(this.settings == null)
            throw GameException.GameNotSetup()

        if(session == null) {
            session = GameSession(this.settings!!)
        }
        session?.setupGame(players)

        if(engine == null)
            engine = GameEngine(session!!)

        engine?.setSession(session!!)
        engine?.startGame()
    }

    fun newSession() {
        val newSessionObj = GameSession(this.settings!!)
        session = newSessionObj
        engine?.setSession(newSessionObj)
    }

    fun getState() = session?.state
    fun getSettings() = this.settings

}
