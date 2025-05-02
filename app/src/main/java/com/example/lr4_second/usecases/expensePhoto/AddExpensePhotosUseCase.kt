package com.example.lr4_second.usecases.expensePhoto

import android.net.Uri
import com.example.lr4_second.datasources.ExpensePhotoDataSource
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.domain.model.PhotoEntity
import javax.inject.Inject

class AddExpensePhotosUseCase @Inject constructor(private val dataSource: ExpensePhotoDataSource) {
    operator fun invoke(photo: PhotoEntity): Result<Unit>
    {
        if (photo.imageUri.isBlank()) return Result.failure(Exception("URI не корректен"))

        dataSource.addPhoto(photo)
        return Result.success(Unit)
    }
}