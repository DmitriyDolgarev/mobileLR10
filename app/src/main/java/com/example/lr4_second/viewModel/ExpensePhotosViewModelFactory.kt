package com.example.lr4_second.viewModel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.lr4_second.usecases.expense.AddExpenseUseCase
import com.example.lr4_second.usecases.expense.DeleteExpenseUseCase
import com.example.lr4_second.usecases.expense.LoadExpensesUseCase
import com.example.lr4_second.usecases.expense.UpdateExpenseUseCase
import com.example.lr4_second.usecases.expensePhoto.AddExpensePhotosUseCase
import com.example.lr4_second.usecases.expensePhoto.DeleteExpensePhotoUseCase
import com.example.lr4_second.usecases.expensePhoto.LoadExpensePhotosUseCase

class ExpensePhotosViewModelFactory(
    private val application: Application,
    private val load: LoadExpensePhotosUseCase,
    private val add: AddExpensePhotosUseCase,
    private val delete: DeleteExpensePhotoUseCase
): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ExpensePhotosViewModel(application, load, add, delete) as T
    }
}