package com.example.sporex_app.useraccount

import android.content.Context
import androidx.core.content.edit
object UserSession {

    private const val PREFS = "sporex_user_session"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_IMAGE = "profile_image"

    fun saveUser(context: Context, username: String?, email: String?) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_USERNAME, username ?: "")
                putString(KEY_EMAIL, email ?: "")
            }
    }

    fun getUsername(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, "You") ?: "You"

    fun getEmail(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, "") ?: ""

    fun saveImage(context: Context, uri: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_IMAGE, uri)
            }
    }

    fun getImage(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_IMAGE, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit { clear() }
    }
}
//object UserSession {
//    private const val PREFS = "sporex_user_session"
//    private const val KEY_USERNAME = "username"
//    private const val KEY_EMAIL = "email"
//
//    fun saveUser(context: Context, username: String?, email: String?) {
//        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//            .edit {
//                putString(KEY_USERNAME, username ?: "")
//                    .putString(KEY_EMAIL, email ?: "")
//            }
//    }
//
//    fun getUsername(context: Context): String {
//        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//            .getString(KEY_USERNAME, "You") ?: "You"
//    }
//
//    fun getEmail(context: Context): String {
//        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//            .getString(KEY_EMAIL, "") ?: ""
//    }
//
//    fun clear(context: Context) {
//        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//            .edit {
//                clear()
//            }
//    }
//
//    private const val KEY_IMAGE = "profile_image"
//
//    fun saveImage(context: Context, uri: String) {
//        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//            .edit {
//                putString(KEY_IMAGE, uri)
//            }
//    }
//
//    fun getImage(context: Context): String? {
//        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//            .getString(KEY_IMAGE, null)
//    }
//}