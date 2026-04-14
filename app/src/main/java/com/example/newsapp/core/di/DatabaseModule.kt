package com.example.newsapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.core.database.AppDatabase
import com.example.newsapp.core.database.dao.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "news_app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: AppDatabase): ArticleDao = db.getArticleDao()
}