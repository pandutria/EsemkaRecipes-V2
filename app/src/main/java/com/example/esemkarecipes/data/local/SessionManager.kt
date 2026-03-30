package com.example.esemkarecipes.data.local

import android.content.Context
import com.example.esemkarecipes.data.model.User
import java.time.LocalDateTime
import java.util.Date

class SessionManager(context: Context) {
//    val pref = "session"
//    val key = "session"
//
//    val shared = context.getSharedPreferences(pref, Context.MODE_PRIVATE)
//
//    fun save(date: LocalDateTime) {
//        val datePlus10 = date.plusMinutes(10)
//        shared.edit().putString(key, datePlus10.toString()).apply()
//    }
//
//    fun get(): String? {
//        return shared.getString(key, null)
//    }
//
//    fun remove() {
//        shared.edit().remove(key).apply()
//    }

    var user: User? = null
}