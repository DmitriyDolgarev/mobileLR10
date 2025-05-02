package com.example.lr4_second.di

import android.content.Context
import com.example.lr4_second.db.Dao
import com.example.lr4_second.db.MainDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): MainDB
    {
        return MainDB.getDB(context)
    }

    @Provides
    fun provideDao(db: MainDB): Dao
    {
        return db.getDao()
    }
}