package com.example.lr4_second.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lr4_second.usecases.expense.AddExpenseUseCase
import com.example.lr4_second.usecases.expense.DeleteExpenseUseCase
import com.example.lr4_second.usecases.expense.LoadExpensesUseCase
import com.example.lr4_second.usecases.expense.UpdateExpenseUseCase

class ExpenseViewModelFactory(
    private val application: Application,
    private val load: LoadExpensesUseCase,
    private val add: AddExpenseUseCase,
    private val update: UpdateExpenseUseCase,
    private val delete: DeleteExpenseUseCase
): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ExpenseViewModel(application, load, add, update, delete) as T
    }
}