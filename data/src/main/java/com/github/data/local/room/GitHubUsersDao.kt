package com.github.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.data.local.model.GitHubUserEntity

@Dao
interface GitHubUsersDao {

    @Query("SELECT * FROM github_users")
    fun getGitHubUsers(): List<GitHubUserEntity>

    @Insert
    fun saveGitHubUsers(vararg users: GitHubUserEntity)

    @Query("UPDATE github_users SET is_favourite = :isFavourite WHERE login = :login")
    fun updateGitHubUserFavourite(login: String, isFavourite: Boolean)
}