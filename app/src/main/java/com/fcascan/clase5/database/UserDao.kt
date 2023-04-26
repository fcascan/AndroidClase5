package com.fcascan.clase5.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fcascan.clase5.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id")
    fun getAllUsers(): MutableList<User?>?

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User?)

    @Update
    fun updateUser(user: User?)

    @Delete
    fun deleteUser(user: User?)
}