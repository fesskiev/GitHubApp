package com.github.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class GitHubUserUi(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val isFavourite: Boolean
)