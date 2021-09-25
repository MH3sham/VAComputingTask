package com.mahmoudh3sham.vatask.di.module

import androidx.core.util.Supplier
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoudh3sham.vatask.ViewModelProviderFactory
import com.mahmoudh3sham.vatask.data.DataManager
import com.mahmoudh3sham.vatask.ui.base.BaseActivity
import com.mahmoudh3sham.vatask.ui.location.MyLocationViewModel
import com.mahmoudh3sham.vatask.ui.main.MainViewModel
import com.mahmoudh3sham.vatask.ui.main.adapter.TasksAdapter
import com.mahmoudh3sham.vatask.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity<*>?) {

    @Provides
    fun provideLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }
    @Provides
    fun provideTasksAdapter(): TasksAdapter {
        return TasksAdapter(ArrayList())
    }

    @Provides
    fun provideMainViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): MainViewModel {
        val supplier: Supplier<MainViewModel> =
            Supplier { MainViewModel(dataManager, schedulerProvider) }
        val factory: ViewModelProviderFactory<MainViewModel> =
            ViewModelProviderFactory(MainViewModel::class.java, supplier)
        return ViewModelProvider(this.activity!!, factory).get(MainViewModel::class.java)
    }

    @Provides
    fun provideMapViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): MyLocationViewModel {
        val supplier: Supplier<MyLocationViewModel> =
            Supplier { MyLocationViewModel(dataManager, schedulerProvider) }
        val factory: ViewModelProviderFactory<MyLocationViewModel> =
            ViewModelProviderFactory(MyLocationViewModel::class.java, supplier)
        return ViewModelProvider(this.activity!!, factory).get(MyLocationViewModel::class.java)
    }
}
