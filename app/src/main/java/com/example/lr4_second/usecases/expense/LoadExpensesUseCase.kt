package com.example.lr4_second.usecases.expense

import com.example.lr4_second.datasources.ExpenseDataSource
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.domain.model.ExpenseModel
import kotlinx.coroutines.flow.Flow

class LoadExpensesUseCase(private val dataSource: ExpenseDataSource) {
    operator fun invoke(): Result<Flow<List<ExpenseModel>>> = runCatching {
        dataSource.getExpenses()
    }
}