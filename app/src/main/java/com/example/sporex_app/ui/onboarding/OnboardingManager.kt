package com.example.sporex_app.ui.onboarding

import android.content.Context

class OnboardingPageOne(context: Context) {

    private val prefs = context.getSharedPreferences("sporex_prefs", Context.MODE_PRIVATE)

    fun isOnboarded(email: String): Boolean {
        return prefs.getBoolean("onboarded_$email", false)
    }

    fun setOnboarded(email: String) {
        prefs.edit()
            .putBoolean("onboarded_$email", true)
            .apply()
    }
}