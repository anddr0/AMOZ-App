package com.example.amoz.api.managers

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthManagerModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseManager(): FirebaseManager {
        return FirebaseManager()
    }

    @Singleton
    @Provides
    fun provideGoogleAuthManager(@ApplicationContext context: Context, tokenManager: TokenManager): GoogleAuthManager {
        return GoogleAuthManager(context, tokenManager)
    }
}
