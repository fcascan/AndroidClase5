package com.fcascan.clase5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fcascan.clase5.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao?

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "users_db"

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE
        }

        private fun buildDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .addCallback(StartingUsers(context))
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}