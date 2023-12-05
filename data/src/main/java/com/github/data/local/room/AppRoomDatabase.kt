package com.github.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.data.local.model.GitHubUserEntity

@Database(entities = [GitHubUserEntity::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun userDao(): GitHubUsersDao
}

val roomDb: (Context) -> AppRoomDatabase = {
    Room.databaseBuilder(it, AppRoomDatabase::class.java, "github_users_db").build()
}