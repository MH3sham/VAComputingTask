package com.mahmoudh3sham.vatask.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mahmoudh3sham.vatask.data.models.EquationModel

class Converters {
    @TypeConverter
    fun fromEQ2JsonStr(equationModel: EquationModel?): String? {
        return Gson().toJson(equationModel)
    }

    @TypeConverter
    fun fromJsonStr2EQ(string: String?): EquationModel? {
        return Gson().fromJson(string, EquationModel::class.java)
    }
}