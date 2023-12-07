package com.github.domain.usecase

import com.github.domain.model.GitHubUser
import com.github.domain.repository.Repository
import javax.inject.Inject

class GetGroupedGitHubUsersUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Result<Map<Char, List<GitHubUser>>> =
        runCatching {
            repository.getGithubUserList()
                .groupBy { user -> user.login[0] }
        }
}