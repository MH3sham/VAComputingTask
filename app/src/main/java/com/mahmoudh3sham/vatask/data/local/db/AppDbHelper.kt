package com.mahmoudh3sham.vatask.data.local.db
import com.mahmoudh3sham.vatask.data.models.EquationModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val mAppDatabase: AppDatabase) : DbHelper {

    override fun getPendingEquations(): Single<List<EquationModel>> {
        return mAppDatabase.equationDao().getPendingEquations()
    }

    override fun getDoneEquations(): Single<List<EquationModel>> {
        return mAppDatabase.equationDao().getDoneEquations()
    }

    override fun insertEquation(equation: EquationModel): Completable {
        return mAppDatabase.equationDao().insert(equation)
    }

    override fun updateEquation(id: Long, status: Boolean, result: Double): Completable {
        return mAppDatabase.equationDao().update(id, status, result)
    }


}