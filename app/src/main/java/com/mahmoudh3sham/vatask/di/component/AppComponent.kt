package com.mahmoudh3sham.vatask.di.component

import android.app.Application
import com.mahmoudh3sham.vatask.MvvmApp
import com.mahmoudh3sham.vatask.data.DataManager
import com.mahmoudh3sham.vatask.di.module.AppModule
import com.mahmoudh3sham.vatask.utils.rx.SchedulerProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: MvvmApp)
    val dataManager: DataManager
    val schedulerProvider: SchedulerProvider

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
