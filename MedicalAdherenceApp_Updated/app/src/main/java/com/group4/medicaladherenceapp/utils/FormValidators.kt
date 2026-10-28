package com.group4.medicaladherenceapp.utils

/** Pure validation rules shared by app forms and local unit tests. */
object FormValidators {
    fun registrationError(fullName: String, email: String, password: String): String? = when {
        fullName.isBlank() || email.isBlank() || password.isBlank() ->
            "Please fill in all required fields"
        password.length < 6 ->
            "Password must be at least 6 characters"
        else -> null
    }

    fun medicationError(name: String, dosage: String, time: String): String? = when {
        name.isBlank() || dosage.isBlank() || time.isBlank() ->
            "Complete all fields"
        !isValidTime(time) ->
            "Enter a valid time in HH:mm format"
        else -> null
    }

    private fun isValidTime(value: String): Boolean {
        val match = Regex("^(\\d{2}):(\\d{2})$").matchEntire(value) ?: return false
        val hour = match.groupValues[1].toInt()
        val minute = match.groupValues[2].toInt()
        return hour in 0..23 && minute in 0..59
    }
}
