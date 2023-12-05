package com.github.data.remote

import com.github.data.remote.model.GitHubUserDetailsDto
import com.github.data.remote.model.GitHubUserDto

interface NetworkSource {

    suspend fun getGitHubUserList(): List<GitHubUserDto>

    suspend fun getGitHubUserDetails(login:String): GitHubUserDetailsDto
}