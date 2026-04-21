package com.example.sporex_app.ui.onboarding

import android.content.Context

class OnboardingPageOne (context: Context){

    private val prefs = context.getSharedPreferences("sporex_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch() : Boolean{
        return prefs.getBoolean("first_launch", true)
    }

    fun finishOnboarding(){
        prefs.edit().putBoolean("first_launch", false).apply()
    }
}