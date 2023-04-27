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

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserIdByEmail(email: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User?) : Long

    @Update
    fun updateUser(user: User?) : Int

    @Delete
    fun deleteUser(user: User?) : Int
}