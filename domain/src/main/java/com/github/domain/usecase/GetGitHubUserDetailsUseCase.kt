package com.github.domain.usecase

import com.github.domain.model.GitHubUserDetails
import com.github.domain.repository.Repository
import javax.inject.Inject

class GetGitHubUserDetailsUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(login: String): Result<GitHubUserDetails> =
        runCatching {
            if (login.isEmpty()) {
                error("login must not be empty!")
            }
            repository.getGitHubUserDetails(login)
        }
}