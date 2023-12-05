package com.github.data.repository

import com.github.data.local.LocalSource
import com.github.data.local.mapper.mapToDomainModel
import com.github.data.remote.NetworkSource
import com.github.data.remote.mapper.mapToDBModel
import com.github.data.remote.mapper.mapToDomainModel
import com.github.domain.model.GitHubUser
import com.github.domain.model.GitHubUserDetails
import com.github.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val networkSource: NetworkSource,
    private val localSource: LocalSource
) : Repository {

    override suspend fun getGithubUserList(): List<GitHubUser> {
        val localUsers = localSource.getGitHubUserList()
        if (localUsers.isNotEmpty()) {
            println("RETURN LOCAL USERS")
            return localUsers.map { it.mapToDomainModel() }
        }
        val remoteUsers = networkSource.getGitHubUserList()
        localSource.saveGitHubUsers(remoteUsers.map { it.mapToDBModel() })
        println("RETURN REMOTE USERS")
        return remoteUsers.map { it.mapToDomainModel() }
    }

    override suspend fun getGitHubUserDetails(login: String): GitHubUserDetails {
        return networkSource.getGitHubUserDetails(login).mapToDomainModel()
    }

    override suspend fun updateGitHubUserFavourite(login: String, isFavourite: Boolean) {
        localSource.updateGitHubUserFavourite(login, isFavourite)
    }
}
