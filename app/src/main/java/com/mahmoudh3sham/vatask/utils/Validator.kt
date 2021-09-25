package com.mahmoudh3sham.vatask.utils

open class Validator {
    fun isAlpha(str: String): Boolean {
        return str.matches(Regex.fromLiteral("^[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z ]*$"))
        //return true;
    }

    fun isNumbersOnly(str: String): Boolean {
        return str.matches(Regex.fromLiteral("^[0-9]+$"))
    }

    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex.fromLiteral("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
    }

    fun isValidMobile(mobile: String): Boolean {
        return mobile.matches(Regex.fromLiteral("^\\+?\\d+$"))
    }
}