package com.example.lr4_second.usecases.expense

import com.example.lr4_second.datasources.ExpenseDataSource

class DeleteExpenseUseCase(private val dataSource: ExpenseDataSource) {
    operator fun invoke(name: String): Result<Unit>
    {
        if (name.isBlank()) return Result.failure(Exception("Название расхода не должно быть пустым"))

        dataSource.deleteExpense(name)
        return Result.success(Unit)
    }
}