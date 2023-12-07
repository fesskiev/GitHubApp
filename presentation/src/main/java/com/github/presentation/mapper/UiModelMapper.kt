package com.github.presentation.mapper

import com.github.domain.model.GitHubUser
import com.github.presentation.model.GitHubUserUi

fun GitHubUser.mapToUiModel(): GitHubUserUi =
    GitHubUserUi(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        isFavourite = isFavourite
    )

fun Map<Char, List<GitHubUser>>.mapToUiModel(): Map<Char, List<GitHubUserUi>> =
    values.flatten()
        .map { user -> user.mapToUiModel() }
        .groupBy { user -> user.login[0] }
