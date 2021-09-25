package com.mahmoudh3sham.vatask.data.local.db

import com.mahmoudh3sham.vatask.data.models.EquationModel
import io.reactivex.Completable
import io.reactivex.Single

interface DbHelper {
    fun getPendingEquations(): Single<List<EquationModel>>
    fun getDoneEquations(): Single<List<EquationModel>>
    fun insertEquation(equation: EquationModel): Completable
    fun updateEquation(id: Long, status:Boolean, result: Double): Completable
}