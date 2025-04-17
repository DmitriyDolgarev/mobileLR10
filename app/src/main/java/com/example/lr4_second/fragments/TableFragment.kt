package com.example.lr4_second.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.lr4_second.R
import com.example.lr4_second.datasources.ExpenseDataSource
import com.example.lr4_second.db.DBWorker
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.usecases.expense.AddExpenseUseCase
import com.example.lr4_second.usecases.expense.DeleteExpenseUseCase
import com.example.lr4_second.usecases.expense.LoadExpensesUseCase
import com.example.lr4_second.usecases.expense.UpdateExpenseUseCase
import com.example.lr4_second.viewModel.ExpenseViewModel
import com.example.lr4_second.viewModel.ExpenseViewModelFactory
import kotlinx.coroutines.launch

class TableFragment: Fragment() {

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var text = view.findViewById<TextView>(R.id.textView3)

        val application = requireActivity().application as Application

        val db = MainDB.getDB(requireContext())

        val expensesDataSource: ExpenseDataSource = ExpenseDataSource(DBWorker(db))

        val addExpenseUseCase = AddExpenseUseCase(expensesDataSource)
        val loadExpensesUseCase = LoadExpensesUseCase(expensesDataSource)
        val updateExpenseUseCse = UpdateExpenseUseCase(expensesDataSource)
        val deleteExpenseUseCase = DeleteExpenseUseCase(expensesDataSource)

        val factory = ExpenseViewModelFactory(
            application,
            loadExpensesUseCase,
            addExpenseUseCase,
            updateExpenseUseCse,
            deleteExpenseUseCase
        )

        viewModel = ViewModelProvider(this, factory).get(ExpenseViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch {

            text.text = ""
            viewModel.loadExpenses()

            viewModel.expenses.collect {expensesList ->
                text.text = ""

                for (expense in expensesList)
                {
                    text.append("${expense.expName}: ${expense.expValue};\n")
                }
            }
        }

    }
}