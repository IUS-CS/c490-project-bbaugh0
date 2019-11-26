package edu.ius.c490.c490project

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executors

private const val TAG = "BudgetRepository"

class BudgetRepository {

    private val db = FirebaseFirestore.getInstance()
    private val executor = Executors.newSingleThreadExecutor()


    fun getBudgets(handler: GetBudgetsHandler) {
        executor.execute {
            db.collection("BudgetsCollection")
                .get()
                .addOnSuccessListener {
                    if (it == null) {
                        handler.onGetBudgets(null)
                    } else {
                        handler.onGetBudgets(it.toObjects(Budget::class.java))
                    }
                }
        }

    }

    fun getBudget(handler: GetBudgetHandler, id: String){
        executor.execute{
            db.collection("BudgetsCollection")
                .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener {
                    if(it == null){
                        handler.onGetBudget(null)
                    } else {
                        handler.onGetBudget(it.documents[0].toObject(Budget::class.java))
                    }
                }
        }
    }

    fun updateBudget(budget: Budget){
        executor.execute{
            db.collection("BudgetsCollection")
                .whereEqualTo("id", budget.id)
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        db.collection("BudgetsCollection")
                            .document(it.id)
                            .update(
                                hashMapOf(
                                    "title" to budget.title,
                                    "maxBudget" to budget.maxBudget,
                                    "currentBudget" to budget.currentBudget
                                ) as Map<String, Any>
                            )
                            .addOnSuccessListener {
                                Log.d(TAG, "update worked")
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "Update failed")
                            }
                    }
                }
        }
    }

    fun insertBudget(budget: Budget) {
        executor.execute {
            db.collection("BudgetsCollection")
                .add(budget)
                .addOnSuccessListener {
                    Log.d(TAG, "added ${budget}")
                }
                .addOnFailureListener {
                    Log.d(TAG, "failed to add ${budget}")
                }
        }
    }

    interface GetBudgetsHandler {
        fun onGetBudgets(budgets: List<Budget>?)
    }

    interface GetBudgetHandler {
        fun onGetBudget(budget: Budget?)
    }
}