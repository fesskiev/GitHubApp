package com.github.domain.model

data class GitHubUserDetails(
    val id: Int,
    val login: String,
    val name: String?,
    val avatarUrl: String,
    val location: String?,
    val isFavourite: Boolean
)