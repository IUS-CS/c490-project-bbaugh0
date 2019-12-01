package com.example.budgetProject

import android.app.Application

class BudgetViewApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BudgetRepository.initialize(this)
    }
}