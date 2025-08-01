package com.adri833.orpheus.data.modules

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.adri833.orpheus.data.player.PlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun providePlayerManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): PlayerManager {
        return PlayerManager(context, player)
    }
}