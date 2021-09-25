package com.mahmoudh3sham.vatask.data

import android.content.Context
import com.google.gson.Gson
import com.mahmoudh3sham.vatask.data.local.db.DbHelper
import com.mahmoudh3sham.vatask.data.models.EquationModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
    val mContext: Context,
    private val mDbHelper: DbHelper
): DataManager {

    override fun setUserAsLoggedOut() {}


    override fun getPendingEquations(): Single<List<EquationModel>> {
        return mDbHelper.getPendingEquations()
    }

    override fun getDoneEquations(): Single<List<EquationModel>> {
        return mDbHelper.getDoneEquations()
    }

    override fun insertEquation(equation: EquationModel): Completable {
        return mDbHelper.insertEquation(equation)
    }

    override fun updateEquation(id: Long, status: Boolean, result: Double): Completable {
        return mDbHelper.updateEquation(id, status, result)
    }
}