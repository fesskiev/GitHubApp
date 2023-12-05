package com.github.domain.usecase

import com.github.domain.repository.Repository
import javax.inject.Inject

class UpdateGitHubUserFavouriteUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(login: String, isFavourite: Boolean): Result<Unit> =
        runCatching {
            repository.updateGitHubUserFavourite(login, isFavourite)
        }
}