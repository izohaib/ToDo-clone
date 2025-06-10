package com.example.todo.core.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.local.TaskDao
import com.example.todo.data.local.TodoDatabase
import com.example.todo.data.repo.TaskRepository
import com.example.todo.data.repo.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideTaskDao(db: TodoDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(context, TodoDatabase::class.java, "todo_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(dao)
    }
}