package com.example.lr4_second.db

import com.example.lr4_second.domain.mappers.ExpenseMapper
import com.example.lr4_second.domain.mappers.PhotoMapper
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.domain.model.ImageModel
import com.example.lr4_second.domain.model.PhotoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DBWorker @Inject constructor(private val db: MainDB) {

    fun getExpenses(): Flow<List<ExpenseModel>>
    {
        return db.getDao().getAllItems().map { roomEntities ->
            roomEntities.map { ExpenseMapper.toDomain(it) }
        }
    }

    fun addExpense(expense: ExpenseModel)
    {
        return db.getDao().insertItem(ExpenseMapper.toRoomItem(expense))
    }

    fun updateExpenseByName(name: String, newName: String, newValue: String)
    {
        db.getDao().updateItem(name, newName, newValue)
    }

    fun deleteExpense(name: String)
    {
        db.getDao().deleteItemByName(name)
    }

    fun getExpenseImages(selectedItem: String): Flow<List<ImageModel>>
    {
        return db.getDao().getExpensePhotos(selectedItem).map { roomEntities ->
            roomEntities.map { PhotoMapper.toImageModel(it) }
        }
    }

    fun addImage(photo: PhotoEntity)
    {
        db.getDao().insertPhoto(PhotoMapper.toRoomItem(photo))
    }

    fun deleteImage(id: Int)
    {
        db.getDao().deletePhotoById(id)
    }
}