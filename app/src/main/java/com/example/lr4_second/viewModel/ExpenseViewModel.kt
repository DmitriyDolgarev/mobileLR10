package com.example.lr4_second.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.usecases.expense.AddExpenseUseCase
import com.example.lr4_second.usecases.expense.DeleteExpenseUseCase
import com.example.lr4_second.usecases.expense.LoadExpensesUseCase
import com.example.lr4_second.usecases.expense.UpdateExpenseUseCase
import com.example.lr4_second.usecases.expensePhoto.AddExpensePhotosUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    application: Application,
    private val loadExpensesUseCase: LoadExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
): AndroidViewModel(application) {

    private val _expenses = MutableStateFlow<List<ExpenseModel>>(emptyList())
    val expenses: StateFlow<List<ExpenseModel>> = _expenses.asStateFlow()

    fun loadExpenses()
    {
        viewModelScope.launch {
            loadExpensesUseCase().onSuccess { flow ->
                flow.collect { expensesList ->
                    _expenses.value = expensesList
                }
            }
        }
    }

    fun addExpense(name: String, value: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            addExpenseUseCase(ExpenseModel(null, name, value))
            //loadExpenses()
        }
    }

    fun updateExpense(name: String, newName: String, newValue: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            updateExpenseUseCase(name, newName, newValue)
            //loadExpenses()
        }
    }

    fun deleteExpense(name: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            deleteExpenseUseCase(name)
            //loadExpenses()
        }
    }
}