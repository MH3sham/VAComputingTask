package com.mahmoudh3sham.vatask.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahmoudh3sham.vatask.data.local.db.dao.EquationDao
import com.mahmoudh3sham.vatask.data.models.EquationModel


@Database(
    entities = [EquationModel::class],
    version = 1
)
@TypeConverters(
    Converters::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun equationDao(): EquationDao
}