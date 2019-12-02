package com.example.budgetProject

import androidx.lifecycle.ViewModel;

class BudgetListViewModel : ViewModel() {
    var name: String = ""
    private val budgetRepository = BudgetRepository.get()
    fun budgetList(handler: BudgetRepository.GetBudgetsHandler) {
        budgetRepository.getBudgets(handler, name)
    }
}

