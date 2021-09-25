package com.mahmoudh3sham.vatask.utils

import com.mahmoudh3sham.vatask.R

object FormValidator : Validator() {
    fun validate(
        fNum: String,
        sNum: String,
        delay: String,
        operator: Int
    ): Int {
        return when {
            fNum.isEmpty() -> {
                R.string.enter_f_error
            }
            sNum.isEmpty() -> {
                R.string.enter_s_error
            }
            operator == AppConstants.NONE -> {
                R.string.enter_operator_error
            }
            delay.isEmpty() -> {
                R.string.enter_delay_error
            }
            delay.toInt() == 0 -> {
                R.string.enter_valid_delay_error
            }
            operator == AppConstants.DIVISION && sNum.toInt() == 0 -> {
                R.string.div_error
            }
            else -> -1
        }
    }
}