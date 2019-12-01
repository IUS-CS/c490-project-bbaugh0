package com.example.budgetProject

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG = "BudgetListFragment"

class BudgetListFragment : Fragment(), BudgetRepository.GetBudgetsHandler {

    private lateinit var budgetRecyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private var adapter: BudgetAdapter? = BudgetAdapter(emptyList())

    companion object {
        fun newInstance() = BudgetListFragment()
    }

    private val viewModel: BudgetListViewModel by lazy {
        ViewModelProviders.of(this).get(BudgetListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.budget_list_fragment, container, false)

        budgetRecyclerView = view.findViewById(R.id.budget_recycler_View)
        budgetRecyclerView.layoutManager = LinearLayoutManager(context)
        budgetRecyclerView.adapter = adapter

        addButton = view.findViewById(R.id.add_budget)
        addButton.setOnClickListener {
            callbacks?.onNewBudget()
        }

        return view
    }

    override fun onGetBudgets(budgets: List<Budget>?) {
        if (budgets == null) {
            Log.d(TAG, "Unable to get budgets?")
            return
        }
        Log.d(TAG, "Got budgets: ${budgets}")
        for (budget in budgets) {
            Log.d(TAG, "Budget ${id}: ${budget.title}")
        }
        updateUI(budgets!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.budgetList(this)
    }

    private fun updateUI(budgets: List<Budget>) {
        adapter = BudgetAdapter(budgets)
        budgetRecyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    private inner class BudgetHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val titleTextView = itemView.findViewById<TextView>(R.id.budget_title)
        private val budgetTextView = itemView.findViewById<TextView>(R.id.current_budget)
        private val maxBudgetTextView = itemView.findViewById<TextView>(R.id.max_budget)
        private lateinit var budget: Budget

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            callbacks?.onBudgetSelected(budget.id.toString())
        }

        fun bind(budget: Budget) {
            this.budget = budget
            titleTextView.text = this.budget.title
            budgetTextView.text = "Current amount spent: $${this.budget.currentBudget}"
            maxBudgetTextView.text = "Max allotted: $${this.budget.maxBudget}"



        }
    }

    private inner class BudgetAdapter(var budgets: List<Budget>) : RecyclerView.Adapter<BudgetHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetHolder {
            val view = layoutInflater.inflate(R.layout.list_item_budget, parent, false)
            return BudgetHolder(view)
        }

        override fun getItemCount(): Int = budgets.size

        override fun onBindViewHolder(holder: BudgetHolder, position: Int) {
            holder.bind(budgets[position])
        }

    }

    interface Callbacks {
        fun onBudgetSelected(budgetId: String)
        fun onNewBudget()
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}
