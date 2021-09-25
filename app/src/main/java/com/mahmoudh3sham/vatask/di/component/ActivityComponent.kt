package com.mahmoudh3sham.vatask.di.component

import com.mahmoudh3sham.vatask.ui.main.MainActivity
import com.mahmoudh3sham.vatask.di.module.ActivityModule
import com.mahmoudh3sham.vatask.di.scope.ActivityScope
import com.mahmoudh3sham.vatask.ui.location.MyLocationActivity
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: MyLocationActivity)
}