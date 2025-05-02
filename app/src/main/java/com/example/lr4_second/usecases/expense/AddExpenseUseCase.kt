package com.example.lr4_second.usecases.expense

import com.example.lr4_second.datasources.ExpenseDataSource
import com.example.lr4_second.domain.model.ExpenseModel
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(private val dataSource: ExpenseDataSource) {
    operator fun invoke(expense: ExpenseModel): Result<Unit>
    {
        if (expense.isEmpty())
        {
            return Result.failure(Exception("Расход не может быть пустым"))
        }

        dataSource.addExpense(expense)
        return Result.success(Unit)
    }
}