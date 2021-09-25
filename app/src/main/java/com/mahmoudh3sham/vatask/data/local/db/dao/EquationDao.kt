package com.mahmoudh3sham.vatask.data.local.db.dao

import androidx.room.*
import com.mahmoudh3sham.vatask.data.models.EquationModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface EquationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(equation: EquationModel): Completable

    @Query("UPDATE equations_table SET status=:status, result=:result WHERE id = :id")
    fun update(id: Long, status: Boolean = true, result: Double): Completable

    @Query("select * from equations_table WHERE status = :status")
    fun getPendingEquations(status: Boolean = false): Single<List<EquationModel>>

    @Query("select * from equations_table WHERE status = :status")
    fun getDoneEquations(status: Boolean = true): Single<List<EquationModel>>
}