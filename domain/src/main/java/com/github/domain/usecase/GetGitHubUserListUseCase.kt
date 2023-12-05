package com.github.domain.usecase

import com.github.domain.model.GitHubUser
import com.github.domain.repository.Repository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetGitHubUserListUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(): Result<List<GitHubUser>> =
        runCatching {
            repository.getGithubUserList()
        }
}