package com.example.lr4_second.fragments

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.adapter.ExpenseAdapter
import com.example.lr4_second.datasources.ExpenseDataSource
import com.example.lr4_second.db.DBWorker
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.usecases.expense.AddExpenseUseCase
import com.example.lr4_second.usecases.expense.DeleteExpenseUseCase
import com.example.lr4_second.usecases.expense.LoadExpensesUseCase
import com.example.lr4_second.usecases.expense.UpdateExpenseUseCase
import com.example.lr4_second.viewModel.ExpenseViewModel
import com.example.lr4_second.viewModel.ExpenseViewModelFactory
import kotlinx.coroutines.launch

class ExpensesFragment: Fragment() {

    lateinit var adapter: ExpenseAdapter
    lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_expenses, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var list: ArrayList<ExpenseModel> = ArrayList()

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

        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.loadExpenses()

            viewModel.expenses.collect {expensesList ->
                list.clear()  // Очищаем список перед добавлением новых данных

                for (expense in expensesList)
                {
                    list.add(expense)
                }

                initial(view, list)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            100 -> {
                showUpdateDialog(item.groupId)
                true
            }
            101 -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.deleteItem(item.groupId, viewModel)
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Метод для отображения диалога обновления
    private fun showUpdateDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Обновить расход")

        val input = EditText(requireContext())
        input.hint = "Название,сумма"

        builder.setView(input)

        builder.setPositiveButton("Обновить") { dialog, _ ->

            if (input.text.toString().isNotEmpty() && input.text.toString().contains(',') && input.text.toString().last() != ',') {

                var newExpenseName = input.text.toString().split(",")[0]
                var newExpenseValue = input.text.toString().split(",")[1]

                if (newExpenseValue.contains(' '))
                {
                    newExpenseValue = newExpenseValue.replace(" ", "")
                }

                if (newExpenseValue.all { it.isDigit() })
                {
                    if (newExpenseName.isNotEmpty() && newExpenseValue.isNotEmpty()) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            adapter.updateItem(position, newExpenseName, newExpenseValue, viewModel)
                        }
                    }
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Закрыть") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun initial(view: View, list: ArrayList<ExpenseModel>)
    {
        recyclerView = view.findViewById(R.id.expensesRv_expenses)
        adapter = ExpenseAdapter()
        recyclerView.adapter = adapter

        adapter.setList(list)
    }
}