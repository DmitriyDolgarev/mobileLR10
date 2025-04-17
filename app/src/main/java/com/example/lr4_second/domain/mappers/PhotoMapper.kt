package com.example.lr4_second.domain.mappers

import com.example.lr4_second.db.ExpensePhoto
import com.example.lr4_second.db.Photos
import com.example.lr4_second.domain.model.ImageModel
import com.example.lr4_second.domain.model.PhotoEntity

object PhotoMapper {
    fun toDomain(roomItem: Photos): PhotoEntity {
        return PhotoEntity(
            roomItem.expenseItemId,
            roomItem.imageUri
        )
    }

    fun toRoomItem(domain: PhotoEntity): Photos {
        return Photos(
            null,
            domain.expenseId,
            domain.imageUri
        )
    }

    fun toImageModel(entity: ExpensePhoto): ImageModel
    {
        return ImageModel(
            entity.id,
            entity.imageUri
        )
    }
}