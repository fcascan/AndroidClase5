package com.fcascan.clase5.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fcascan.clase5.R
import com.fcascan.clase5.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader

class StartingUsers(private val context: Context) : RoomDatabase.Callback() {

    private val USERS_JSON = "users.json"

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("StartingUsers", "Pre-populating database...")
            fillWithStartingUsers(context)
        }
    }


    private fun fillWithStartingUsers(context: Context) {
        val dao = AppDatabase.getInstance(context)?.userDao()

        try {
            val users = loadJSONFromAsset(context,  R.raw.users)
            for (i in 0 until users.length()) {
                val item = users.getJSONObject(i)
                val user = User(
                    item.getInt("id"),
                    item.getString("name"),
                    item.getString("lastName"),
                    item.getString("email"),
                    item.getString("password")
                )
            }
        } catch (e: JSONException) {
            Log.e("fillWithStartingUsers", e.toString())
        }
    }

    private fun loadJSONFromAsset(context: Context, file: Int): JSONArray {
        val inputStream = context.resources.openRawResource(file)

        BufferedReader(inputStream.reader()).use { reader ->
            return JSONArray(reader.readText())
        }
    }
}