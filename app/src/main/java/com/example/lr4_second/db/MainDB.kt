package com.example.lr4_second.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [ExpenseItem::class, Photos::class], version = 2)
abstract class MainDB: RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        fun getDB(context: Context): MainDB
        {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "main.db"
            ).build()
        }
    }
}