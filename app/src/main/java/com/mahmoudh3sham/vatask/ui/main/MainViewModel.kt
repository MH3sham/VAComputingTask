package com.mahmoudh3sham.vatask.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mahmoudh3sham.vatask.data.DataManager
import com.mahmoudh3sham.vatask.data.models.EquationModel
import com.mahmoudh3sham.vatask.ui.base.BaseViewModel
import com.mahmoudh3sham.vatask.utils.rx.SchedulerProvider
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

class MainViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<MainNavigator?>(dataManager, schedulerProvider) {
    private val TAG = "MainViewModel"

    val insertEqLiveData: MutableLiveData<Int> = MutableLiveData()
    val updateEqLiveData: MutableLiveData<Int> = MutableLiveData()
    private val pendingEqsLiveData: MutableLiveData<List<EquationModel>> = MutableLiveData()
    private val doneEqsLiveData: MutableLiveData<List<EquationModel>> = MutableLiveData()

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun getInsertEqLiveData(): LiveData<Int> {
        return insertEqLiveData
    }
    fun insertEquation(equation: EquationModel) {
        dataManager.insertEquation(equation)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    insertEqLiveData.postValue(200)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                    insertEqLiveData.postValue(400)
                }
            })
    }

    fun getUpdateEqLiveData(): LiveData<Int> {
        return updateEqLiveData
    }
    fun updateEquation(id: Long, status: Boolean, result: Double) {
        dataManager.updateEquation(id, status, result)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    updateEqLiveData.postValue(200)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                    updateEqLiveData.postValue(400)
                }
            })
    }


    fun getPendingEqsLiveData(): LiveData<List<EquationModel>> {
        return pendingEqsLiveData
    }
    @SuppressLint("CheckResult")
    fun getPendingEquations() {
        dataManager.getPendingEquations()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe { response ->
                if (response.isNotEmpty()) {
                    pendingEqsLiveData.value = response
                }
            }
    }

    fun getDoneEqsLiveData(): LiveData<List<EquationModel>> {
        return doneEqsLiveData
    }
    @SuppressLint("CheckResult")
    fun getDoneEquations() {
        dataManager.getDoneEquations()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe { response ->
                if (response.isNotEmpty()) {
                    doneEqsLiveData.value = response
                }
            }
    }


}
