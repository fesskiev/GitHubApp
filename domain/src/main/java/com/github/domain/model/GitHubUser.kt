package com.github.domain.model

data class GitHubUser(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val isFavourite: Boolean
)