package com.yakovskij.mafiacards.features.localgame.data.gamesettings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GameSettingsModule {

    @Binds
    abstract fun bindGameSettingsRepository(
        impl: GameSettingsRepository
    ): IGameSettingsRepository
}