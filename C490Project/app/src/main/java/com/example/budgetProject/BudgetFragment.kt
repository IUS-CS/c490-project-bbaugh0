package com.example.budgetProject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "BudgetFragment"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BudgetFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BudgetFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BudgetFragment : Fragment(), BudgetRepository.GetBudgetHandler {
    // TODO: Rename and change types of parameters
    private var budgetID: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val budgetRepository = BudgetRepository.get()

    private lateinit var titleField: EditText
    private lateinit var maxBudget: EditText
    private lateinit var amountSpent: EditText
    private lateinit var budget: Budget
    private var spent: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            budgetID = it.getSerializable(ARG_PARAM1) as String?
        }
        Log.d(TAG, "onCreate ${budgetID}")
    }

    override fun onGetBudget(budget: Budget?) {
        if (budget != null) {
            this.budget = budget
            updateUI()
        } else {
            Log.e(TAG, "no budget??")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        titleField = view.findViewById(R.id.budget_title)
        maxBudget = view.findViewById(R.id.budget_amount)
        amountSpent = view.findViewById(R.id.amount_spent)
        Log.d(TAG, "onCreateView for ${budgetID}")
        if (budgetID == null) {
            this.budget = Budget()
            this.budget.name = budgetRepository.username
            updateUI()
        } else
            budgetRepository.getBudget(this, budgetID!!)

        return view
    }


    fun updateUI() {
        maxBudget.setText(budget.maxBudget)
        titleField.setText(budget.title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // blank
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                budget.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // blank
            }
        }
        titleField.addTextChangedListener(titleWatcher)

        val titleWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // blank
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                budget.maxBudget = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // blank
            }
        }

        maxBudget.addTextChangedListener(titleWatcher2)

        val titleWatcher3 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // blank
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                spent = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // blank
            }
        }
        amountSpent.addTextChangedListener(titleWatcher3)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStop() {
        super.onStop()
        updateAmountSpent()
        if (budgetID == null)
            budgetRepository.insertBudget(budget)
        else
            budgetRepository.updateBudget(budget)
    }


    fun updateAmountSpent(){
        if(spent.isNullOrBlank()) {
            return
        } else {
            var temp = spent
            val moneySpent = temp?.toDoubleOrNull()
            var currentAmount = budget.currentBudget?.toDoubleOrNull()
            if(currentAmount == null || moneySpent == null) {
                return
            }

            currentAmount += moneySpent

            budget.currentBudget = currentAmount.toString()
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BudgetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(budgetId: String?, name: String?) =
            BudgetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, budgetId)
                    putSerializable(ARG_PARAM2, name)
                }
            }
    }
}
