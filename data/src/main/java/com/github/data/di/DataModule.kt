package com.github.data.di

import android.content.Context
import com.github.data.local.LocalSource
import com.github.data.local.LocalSourceImpl
import com.github.data.local.room.AppRoomDatabase
import com.github.data.local.room.roomDb
import com.github.data.remote.NetworkSource
import com.github.data.remote.NetworkSourceImpl
import com.github.data.remote.ktor.ktorClient
import com.github.data.repository.RepositoryImpl
import com.github.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNetworkSource(networkSourceImpl: NetworkSourceImpl): NetworkSource

    @Singleton
    @Binds
    fun bindLocalSource(localSourceImpl: LocalSourceImpl): LocalSource

    @Singleton
    @Binds
    fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    companion object {

        @Singleton
        @Provides
        fun provideRoomDatabase(@ApplicationContext context: Context): AppRoomDatabase = roomDb(context)

        @Singleton
        @Provides
        fun provideKtorClient(): HttpClient = ktorClient

        @Singleton
        @Provides
        fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}