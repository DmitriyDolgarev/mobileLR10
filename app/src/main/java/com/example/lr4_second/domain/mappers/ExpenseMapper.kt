package com.example.lr4_second.domain.mappers

import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.domain.model.ExpenseModel

object ExpenseMapper {
    fun toDomain(roomItem: ExpenseItem): ExpenseModel {
        return ExpenseModel(
            roomItem.id,
            roomItem.expenseName,
            roomItem.expenseValue
        )
    }

    fun toRoomItem(entity: ExpenseModel): ExpenseItem {
        return ExpenseItem(
            null,
            entity.expName,
            entity.expValue
        )
    }
}