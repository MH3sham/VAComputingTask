package com.mahmoudh3sham.vatask

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.mahmoudh3sham.vatask.di.component.AppComponent
import com.mahmoudh3sham.vatask.di.component.DaggerAppComponent

class MvvmApp : Application() {
    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
        appComponent!!.inject(this)
    }

}
