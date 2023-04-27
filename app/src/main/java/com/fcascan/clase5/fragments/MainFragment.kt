package com.fcascan.clase5.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.fcascan.clase5.R
import com.fcascan.clase5.database.AppDatabase
import com.fcascan.clase5.database.UserDao
import com.fcascan.clase5.entities.User
import com.google.android.material.snackbar.Snackbar
import com.wajahatkarim3.roomexplorer.RoomExplorer

class MainFragment : Fragment() {
    private val DATABASE_NAME = "users_db"
    private var db: AppDatabase? = null
    private var userDao: UserDao? = null

    private lateinit var v: View
    private lateinit var edtName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnDelete: Button
    private lateinit var btnEdit: Button
    private lateinit var btnSearch: Button
    private lateinit var btnDebug: Button
    private lateinit var userList: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_main, container, false)
        edtName = v.findViewById(R.id.edt_name)
        edtLastName = v.findViewById(R.id.edt_lastname)
        edtEmail = v.findViewById(R.id.edt_email)
        edtPass = v.findViewById(R.id.edt_pass)
        btnAdd = v.findViewById(R.id.btn_add)
        btnDelete = v.findViewById(R.id.btn_delete)
        btnEdit = v.findViewById(R.id.btn_editar)
        btnSearch = v.findViewById(R.id.btn_search)
        btnDebug = v.findViewById(R.id.btn_debug)
        return v
    }

    override fun onStart() {
        super.onStart()

        db = AppDatabase.getInstance(v.context)
        userDao = db?.userDao()

        // Dummy call to pre-populate db
        userDao?.getAllUsers()

        btnAdd.setOnClickListener {
            //Needs all fields completed to execute
            if (checkEmptyFields()) {
                Log.d("MainFragment", "User NOT added, empty fields.")
                return@setOnClickListener
            }
            val userId = userDao?.getUserIdByEmail(edtEmail.text.toString())
            if (userId != 0) {
                Snackbar.make(v, "Email already in use. Try again with another email.", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT added, mail already in use.")
            } else {
                userDao?.insertUser(User(0, edtName.text.toString(), edtLastName.text.toString(), edtEmail.text.toString(), edtPass.text.toString()))
                Snackbar.make(v, "User added successfully", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User added successfully")
            }
        }

        btnDelete.setOnClickListener {
            //Only needs Email and Password fields completed to execute
            val user = userDao?.getUserByEmailAndPassword(edtEmail.text.toString(), edtPass.text.toString())
            if (user == null) {
                Snackbar.make(v, "User not found", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT found, nothing was deleted")
                return@setOnClickListener
            }
            val success = userDao?.deleteUser(user)
            if (success != null && success > 0) {
                Snackbar.make(v, "User deleted successfully", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User deleted")
            } else {
                Snackbar.make(v, "User couldn't be deleted", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT deleted")
            }
        }

        btnEdit.setOnClickListener {
            //Needs all fields completed to execute
            if (checkEmptyFields()) {
                Log.d("MainFragment", "User NOT edited, empty fields.")
                return@setOnClickListener
            }
            val userId = userDao?.getUserIdByEmail(edtEmail.text.toString())
            if (userId == null || userId == 0) {
                Snackbar.make(v, "User not found", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT found, no changes were made")
                return@setOnClickListener
            }
            val success = userDao?.updateUser(User(userId, edtName.text.toString(), edtLastName.text.toString(), edtEmail.text.toString(), edtPass.text.toString()))
            if (success != null && success > 0) {
                Snackbar.make(v, "User updated successfully", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User updated")
            } else {
                Snackbar.make(v, "User couldn't be updated", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT updated")
            }
        }

        btnSearch.setOnClickListener {
            //Only needs Email field to execute
            val userId = userDao?.getUserIdByEmail(edtEmail.text.toString())
            if (userId == null || userId == 0) {
                Snackbar.make(v, "User not found with that email", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT found")
                return@setOnClickListener
            }
            val user = userDao?.getUserById(userId)
            if (user != null) {
                Snackbar.make(v, "User found: ${user.name} ${user.lastName}", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User found: ${user.name} ${user.lastName}")
            } else {
                Snackbar.make(v, "User not found with that email", Snackbar.LENGTH_LONG).show()
                Log.d("MainFragment", "User NOT found")
            }
        }

        btnDebug.setOnClickListener {
            RoomExplorer.show(context, AppDatabase::class.java, DATABASE_NAME)
        }
    }

    fun checkEmptyFields(): Boolean {
        return if(edtName.text.toString().isEmpty() || edtLastName.text.toString().isEmpty() || edtEmail.text.toString().isEmpty() || edtPass.text.toString().isEmpty()) {
            Snackbar.make(v, "Please fill all the fields", Snackbar.LENGTH_LONG).show()
            true
        } else
            false
    }
}