package com.mahmoudh3sham.vatask.ui.location

import com.mahmoudh3sham.vatask.data.DataManager
import com.mahmoudh3sham.vatask.ui.base.BaseViewModel
import com.mahmoudh3sham.vatask.utils.rx.SchedulerProvider

class MyLocationViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MyLocationNavigator?>(dataManager, schedulerProvider) {
}
