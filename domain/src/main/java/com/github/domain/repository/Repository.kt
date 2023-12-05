package com.github.domain.repository

import com.github.domain.model.GitHubUser
import com.github.domain.model.GitHubUserDetails

interface Repository {

    suspend fun getGithubUserList(): List<GitHubUser>

    suspend fun getGitHubUserDetails(login:String): GitHubUserDetails

    suspend fun updateGitHubUserFavourite(login: String, isFavourite: Boolean)
}