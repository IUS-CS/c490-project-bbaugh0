package edu.ius.c490.c490project

import java.util.*


data class Budget(var id: UUID = UUID.randomUUID(),
                  var title: String,
                  var maxBudget: Double,
                  var currentBudget: Double)
