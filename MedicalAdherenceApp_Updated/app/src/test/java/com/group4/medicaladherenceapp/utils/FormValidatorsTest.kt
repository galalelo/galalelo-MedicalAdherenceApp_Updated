package com.group4.medicaladherenceapp.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FormValidatorsTest {
    @Test
    fun registration_rejectsMissingRequiredFields() {
        assertEquals(
            "Please fill in all required fields",
            FormValidators.registrationError("", "person@example.com", "secret1")
        )
        assertEquals(
            "Please fill in all required fields",
            FormValidators.registrationError("Alex", "   ", "secret1")
        )
        assertEquals(
            "Please fill in all required fields",
            FormValidators.registrationError("Alex", "person@example.com", "")
        )
    }

    @Test
    fun registration_rejectsPasswordShorterThanSixCharacters() {
        assertEquals(
            "Password must be at least 6 characters",
            FormValidators.registrationError("Alex", "person@example.com", "12345")
        )
    }

    @Test
    fun registration_acceptsCurrentMinimumValidInput() {
        assertNull(FormValidators.registrationError("Alex", "not-validated-here", "123456"))
    }

    @Test
    fun medication_rejectsBlankNameDosageOrTime() {
        assertEquals("Complete all fields", FormValidators.medicationError("", "10 mg", "08:30"))
        assertEquals("Complete all fields", FormValidators.medicationError("Medicine", "   ", "08:30"))
        assertEquals("Complete all fields", FormValidators.medicationError("Medicine", "10 mg", ""))
    }

    @Test
    fun medication_acceptsValid24HourTime() {
        assertNull(FormValidators.medicationError("Medicine", "10 mg", "00:00"))
        assertNull(FormValidators.medicationError("Medicine", "10 mg", "23:59"))
    }

    @Test
    fun medication_rejectsMalformedOrOutOfRangeTime() {
        listOf("8:30", "24:00", "12:60", "12.30", "noon").forEach { time ->
            assertEquals(
                "Enter a valid time in HH:mm format",
                FormValidators.medicationError("Medicine", "10 mg", time)
            )
        }
    }
}
