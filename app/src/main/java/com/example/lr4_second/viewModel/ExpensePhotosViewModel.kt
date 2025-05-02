package com.example.lr4_second.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr4_second.db.ExpensePhoto
import com.example.lr4_second.domain.model.ImageModel
import com.example.lr4_second.domain.model.PhotoEntity
import com.example.lr4_second.usecases.expensePhoto.AddExpensePhotosUseCase
import com.example.lr4_second.usecases.expensePhoto.DeleteExpensePhotoUseCase
import com.example.lr4_second.usecases.expensePhoto.LoadExpensePhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensePhotosViewModel @Inject constructor(
    private val loadExpensesPhotosUseCase: LoadExpensePhotosUseCase,
    private val addExpensePhotosUseCase: AddExpensePhotosUseCase,
    private val deleteExpensePhotoUseCase: DeleteExpensePhotoUseCase
): ViewModel() {

    private val _expensePhotos = MutableStateFlow<List<ImageModel>>(emptyList())
    val expensePhotos: StateFlow<List<ImageModel>> = _expensePhotos.asStateFlow()

    fun loadExpensePhotos(expenseName: String)
    {
        viewModelScope.launch {
            loadExpensesPhotosUseCase(expenseName).onSuccess { flow ->
                flow.collect { photoslist ->
                    _expensePhotos.value = photoslist
                }
            }
        }
    }

    fun addExpensePhoto(expenseId: Int, imageUri: Uri)
    {
        viewModelScope.launch(Dispatchers.IO) {
            addExpensePhotosUseCase(PhotoEntity(expenseId, imageUri.toString()))
            //loadExpensePhotos(expense)
        }
    }

    fun deleteExpensePhoto(id: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            deleteExpensePhotoUseCase(id)
        }
    }
}