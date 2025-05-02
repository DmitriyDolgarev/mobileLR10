package com.example.lr4_second.usecases.expensePhoto

import com.example.lr4_second.datasources.ExpensePhotoDataSource
import com.example.lr4_second.domain.model.ImageModel
import javax.inject.Inject

class DeleteExpensePhotoUseCase @Inject constructor(private val dataSource: ExpensePhotoDataSource) {
    operator fun invoke(id: Int): Result<Unit>
    {
        dataSource.deletePhoto(id)
        return Result.success(Unit)
    }
}