package com.example.budgetProject

import java.util.*

data class Budget(var id: String = UUID.randomUUID().toString(),
                  var title: String? = "",
                  var maxBudget: String? = "0.0",
                  var currentBudget: String? = "0.0")