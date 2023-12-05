package com.github.data.local.mapper

import com.github.data.local.model.GitHubUserEntity
import com.github.domain.model.GitHubUser

fun GitHubUserEntity.mapToDomainModel(): GitHubUser =
    GitHubUser(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        isFavourite = isFavourite
    )