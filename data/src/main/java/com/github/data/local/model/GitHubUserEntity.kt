package com.github.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_users")
data class GitHubUserEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "avatar_utl")
    val avatarUrl: String,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean
)