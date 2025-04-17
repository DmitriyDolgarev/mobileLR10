package com.example.lr4_second.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.lr4_second.R
import com.example.lr4_second.datasources.ExpenseDataSource
import com.example.lr4_second.db.DBWorker
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.usecases.expense.AddExpenseUseCase
import com.example.lr4_second.usecases.expense.DeleteExpenseUseCase
import com.example.lr4_second.usecases.expense.LoadExpensesUseCase
import com.example.lr4_second.usecases.expense.UpdateExpenseUseCase
import com.example.lr4_second.viewModel.ExpenseViewModel
import com.example.lr4_second.viewModel.ExpenseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.math.exp

class AddingFragment: Fragment() {

    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var viewModel: ExpenseViewModel

    override fun onDestroyView() {
        executor.shutdown() // Важно: очистка ресурсов
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_adding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var btn = view.findViewById<Button>(R.id.addingButton)
        var name = view.findViewById<EditText>(R.id.addingNameText)
        var sum = view.findViewById<EditText>(R.id.addingSumText)

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

        btn.setOnClickListener {

            viewModel.addExpense(name.text.toString(), sum.text.toString())

            name.text.clear()
            sum.text.clear()

            Toast.makeText(requireContext(), "Добавлено: ${name.text}, ${sum.text}", Toast.LENGTH_SHORT).show()

        }
    }
}