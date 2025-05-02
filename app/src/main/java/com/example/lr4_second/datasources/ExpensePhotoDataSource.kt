package com.example.lr4_second.datasources

import com.example.lr4_second.db.DBWorker
import com.example.lr4_second.db.ExpensePhoto
import com.example.lr4_second.domain.model.ImageModel
import com.example.lr4_second.domain.model.PhotoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpensePhotoDataSource @Inject constructor(private val db: DBWorker) {

    fun getExpensePhotos(expenseName: String): Flow<List<ImageModel>>
    {
        return db.getExpenseImages(expenseName)
    }

    fun addPhoto(photo: PhotoEntity)
    {
        db.addImage(photo)
    }

    fun deletePhoto(id: Int)
    {
        db.deleteImage(id)
    }
}