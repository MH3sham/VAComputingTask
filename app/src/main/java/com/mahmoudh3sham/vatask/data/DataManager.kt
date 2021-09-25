package com.mahmoudh3sham.vatask.data

import com.mahmoudh3sham.vatask.data.local.db.DbHelper

interface DataManager : DbHelper {
    fun setUserAsLoggedOut()

    enum class LoggedInMode(val type: Int) {
        LOGGED_IN_MODE_LOGGED_OUT(0), LOGGED_IN_MODE_GOOGLE(1), LOGGED_IN_MODE_FB(2), LOGGED_IN_MODE_SERVER(
            3
        );

    }
}