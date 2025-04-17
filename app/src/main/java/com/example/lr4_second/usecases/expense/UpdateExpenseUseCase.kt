package com.example.lr4_second.usecases.expense

import com.example.lr4_second.datasources.ExpenseDataSource

class UpdateExpenseUseCase(private val dataSource: ExpenseDataSource) {
    operator fun invoke(name: String, newName: String, newValue: String): Result<Unit>
    {
        if (newName.isBlank() || newValue.isBlank()) return Result.failure(Exception("Новые данные не должны быть пустыми"))

        if (newName.contains(Regex("[?.!,;:]")) || newValue.contains(Regex("[?.!,;:]"))) {
            return Result.failure(Exception("Новые данные содержат недопустимые символы"))
        }

        dataSource.updateExpense(name, newName, newValue)
        return Result.success(Unit)
    }
}