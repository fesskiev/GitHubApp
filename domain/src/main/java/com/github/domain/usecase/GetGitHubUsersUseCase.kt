package com.github.domain.usecase

import com.github.domain.model.GitHubUser
import com.github.domain.repository.Repository
import javax.inject.Inject

class GetGitHubUsersUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(): Result<List<GitHubUser>> =
        runCatching {
            repository.getGithubUserList()
        }
}