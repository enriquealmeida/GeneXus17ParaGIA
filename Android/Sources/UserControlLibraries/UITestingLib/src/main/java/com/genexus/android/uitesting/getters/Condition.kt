package com.genexus.android.uitesting.getters

class Condition(private val condition: Boolean) {
    fun verify(message: String?) {
        if (!condition) throw ConditionVerificationException(message)
    }
}
