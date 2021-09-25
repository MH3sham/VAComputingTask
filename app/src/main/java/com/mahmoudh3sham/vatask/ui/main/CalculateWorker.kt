package com.mahmoudh3sham.vatask.ui.main

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mahmoudh3sham.vatask.utils.AppConstants

class CalculateWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    private val TAG = "CalculateWorker"
    override fun doWork(): Result {
        val id = inputData.getLong("id", 0)
        val fNum = inputData.getDouble("fNum", 0.0)
        val sNum = inputData.getDouble("sNum", 0.0)
        val operator = inputData.getInt("operator", 0)

        var result = 0.0

        try {
            when (operator) {
                AppConstants.PLUS -> {
                    result = Calculator.add(fNum, sNum)
                }
                AppConstants.MINUS -> {
                    result = Calculator.sub(fNum, sNum)
                }
                AppConstants.MULTIPLICATION -> {
                    result = Calculator.mul(fNum, sNum)
                }
                AppConstants.DIVISION -> {
                    result = Calculator.div(fNum, sNum)
                }
            }

            Log.e(TAG, "doWork:$id >> $fNum $operator $sNum = $result")

            val outputData = Data.Builder()
                .putLong("id", id)
                .putDouble("result", result)
                .build()


            return Result.success(outputData)

        }catch (e: Exception){
            Log.e(TAG, "doWork: ", e)
            return Result.failure()
        }

    }
}