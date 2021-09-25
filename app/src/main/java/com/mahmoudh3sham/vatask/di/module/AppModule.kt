package com.mahmoudh3sham.vatask.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mahmoudh3sham.vatask.data.AppDataManager
import com.mahmoudh3sham.vatask.data.DataManager
import com.mahmoudh3sham.vatask.data.local.db.AppDatabase
import com.mahmoudh3sham.vatask.data.local.db.AppDbHelper
import com.mahmoudh3sham.vatask.data.local.db.DbHelper
import com.mahmoudh3sham.vatask.di.DatabaseInfo
import com.mahmoudh3sham.vatask.di.PreferenceInfo
import com.mahmoudh3sham.vatask.utils.AppConstants
import com.mahmoudh3sham.vatask.utils.rx.AppSchedulerProvider
import com.mahmoudh3sham.vatask.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @Singleton
    fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@DatabaseInfo dbName: String?, context: Context?): AppDatabase{
        return Room.databaseBuilder(context!!, AppDatabase::class.java, dbName!!)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return AppConstants.DB_NAME
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @PreferenceInfo
    fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

}
