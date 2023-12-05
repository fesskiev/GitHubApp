package com.github.data.local

import com.github.data.local.model.GitHubUserEntity

interface LocalSource {

    suspend fun getGitHubUserList(): List<GitHubUserEntity>

    suspend fun saveGitHubUsers(remoteUsers: List<GitHubUserEntity>)

    suspend fun updateGitHubUserFavourite(login: String, isFavourite: Boolean)
}