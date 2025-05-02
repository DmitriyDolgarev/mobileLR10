package com.example.lr4_second.usecases.expensePhoto

import com.example.lr4_second.datasources.ExpensePhotoDataSource
import com.example.lr4_second.db.ExpensePhoto
import com.example.lr4_second.domain.model.ImageModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadExpensePhotosUseCase @Inject constructor(private val dataSource: ExpensePhotoDataSource) {
    operator fun invoke(expenseName: String): Result<Flow<List<ImageModel>>> = runCatching {

        if (expenseName.isEmpty()) return Result.failure(Exception("Расход не должен быть пустым"))

        dataSource.getExpensePhotos(expenseName)
    }
}