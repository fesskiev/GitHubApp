package com.github.data.local

import com.github.data.local.model.GitHubUserEntity
import com.github.data.local.room.AppRoomDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalSourceImpl @Inject constructor(
    private val roomDatabase: AppRoomDatabase,
    private val dispatcher: CoroutineDispatcher
) : LocalSource {

    override suspend fun getGitHubUserList(): List<GitHubUserEntity> =
        withContext(dispatcher) {
            return@withContext roomDatabase.userDao().getGitHubUsers()
        }

    override suspend fun saveGitHubUsers(remoteUsers: List<GitHubUserEntity>) {
        withContext(dispatcher) {
            roomDatabase.userDao().saveGitHubUsers(*remoteUsers.toTypedArray())
        }
    }

    override suspend fun updateGitHubUserFavourite(login: String, isFavourite: Boolean) {
        withContext(dispatcher) {
            roomDatabase.userDao().updateGitHubUserFavourite(login, isFavourite)
        }
    }
}