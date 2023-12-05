package com.github.data.remote.mapper

import com.github.data.local.model.GitHubUserEntity
import com.github.data.remote.model.GitHubUserDetailsDto
import com.github.data.remote.model.GitHubUserDto
import com.github.domain.model.GitHubUser
import com.github.domain.model.GitHubUserDetails

fun GitHubUserDetailsDto.mapToDomainModel(): GitHubUserDetails =
    GitHubUserDetails(
        id = id,
        login = login,
        name = name,
        avatarUrl = avatarUrl,
        location = location,
        isFavourite = false
    )

fun GitHubUserDto.mapToDomainModel(): GitHubUser =
    GitHubUser(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        isFavourite = false
    )

fun GitHubUserDto.mapToDBModel(): GitHubUserEntity =
    GitHubUserEntity(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        isFavourite = false
    )
