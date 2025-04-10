package com.example.lr4_second.db

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.example.lr4_second.model.ExpenseModel
import com.example.lr4_second.model.ImageModel

class DBWorker(_context: Context) {

    private var context: Context = _context

    fun getExpenses(viewLifecycleOwner: LifecycleOwner): ArrayList<ExpenseModel>
    {
        var list: ArrayList<ExpenseItem> = ArrayList()

        var result = ArrayList<ExpenseModel>()

        val db = MainDB.getDB(context)

        list = db.getDao().getAllItems()

        for (expenseItem in list)
        {
            result.add(ExpenseModel(expenseItem.expenseName, expenseItem.expenseValue))
        }

        /*
        db.getDao().getAllItems().asLiveData().observe(viewLifecycleOwner)
        { itList ->
            list = ArrayList()
            itList.forEach{
                var expense: ExpenseModel = ExpenseModel(it.expenseName, it.expenseValue)
                list.add(expense)
            }
        }
        */


        return result
    }

    fun addExpense(expense: ExpenseItem)
    {
        val db = MainDB.getDB(context)
        db.getDao().insertItem(expense)
    }

    fun updateExpenseByName(name: String, newName: String, newValue: String)
    {
        val db = MainDB.getDB(context)
        db.getDao().updateItem(name, newName, newValue)
    }

    fun deleteExpense(name: String)
    {
        val db = MainDB.getDB(context)
        db.getDao().deleteItemByName(name)
    }

    fun getImages(viewLifecycleOwner: LifecycleOwner, selectedItem: String): ArrayList<ImageModel>
    {
        var imageList = ArrayList<ImageModel>()

        val db = MainDB.getDB(context)
        db.getDao().getExpensePhotos(selectedItem).asLiveData().observe(viewLifecycleOwner)
        { itList ->
            imageList = ArrayList()
            itList.forEach{
                var image = ImageModel(it.id, it.imageUri)
                imageList.add(image)
            }
        }

        return imageList
    }

    fun addImage(photo: Photos)
    {
        val db = MainDB.getDB(context)
        db.getDao().insertPhoto(photo)
    }

    fun getImagesByExpense(name: String)
    {
        val db = MainDB.getDB(context)
        db.getDao().deleteItemByName(name)
    }
}