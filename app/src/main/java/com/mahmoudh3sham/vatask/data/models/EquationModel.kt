package com.mahmoudh3sham.vatask.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equations_table")
data class EquationModel(
    @PrimaryKey var id: Long = 0,
    var fNum: Double = 0.0,
    var sNum: Double = 0.0,
    var operation: String = "",
    var result: Double = 0.0,
    var status: Boolean = false,
    var delay: Int = 0,
)
