package com.fcascan.clase5.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(id: Int, name: String, lastName: String, email: String, password: String){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = id

    @ColumnInfo(name = "name")
    var name: String = name

    @ColumnInfo(name = "lastName")
    var lastName: String = lastName

    @ColumnInfo(name = "email")
    var email: String = email

    @ColumnInfo(name = "password")
    var password: String = password

    init {
        this.id = id
        this.name = name
        this.lastName = lastName
        this.email = email
        this.password = password
    }
}