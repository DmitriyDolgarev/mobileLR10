package com.example.lr4_second.datasources

import android.content.Context
import com.example.lr4_second.db.DBWorker
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.domain.model.ExpenseModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseDataSource @Inject constructor(private val db: DBWorker) {


    fun getExpenses(): Flow<List<ExpenseModel>>
    {
        return db.getExpenses()
    }

    fun addExpense(expense: ExpenseModel)
    {
        db.addExpense(expense)
    }

    fun updateExpense(name: String, newName: String, newvalue: String)
    {
        db.updateExpenseByName(name, newName, newvalue)
    }

    fun deleteExpense(name: String)
    {
        db.deleteExpense(name)
    }
}