package com.example.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.example.taskmanager.data.local.database.TaskDatabase
import com.example.taskmanager.data.remote.api.TaskApi
import com.example.taskmanager.data.repository.TaskRepositoryImpl
import com.example.taskmanager.domain.repository.TaskRepository
import com.example.taskmanager.util.AnalyticsLogger
import com.example.taskmanager.util.AnalyticsLoggerImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskApi(): TaskApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.127.52:3000/") // For Android emulator
            // .baseUrl("http://localhost:3000/") // For physical device (if on same network)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        db: TaskDatabase,
        api: TaskApi
    ): TaskRepository {
        return TaskRepositoryImpl(db.taskDao(), api)
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return Firebase.crashlytics
    }

    @Provides
    @Singleton
    fun provideAnalyticsLogger(
        analytics: FirebaseAnalytics,
        crashlytics: FirebaseCrashlytics
    ): AnalyticsLogger {
        return AnalyticsLoggerImpl(analytics, crashlytics)
    }
}