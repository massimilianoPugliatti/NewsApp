package com.example.newsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.core.database.converters.LocalDateTimeConverter
import com.example.newsapp.core.database.dao.ArticleDao
import com.example.newsapp.core.database.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = true)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}

