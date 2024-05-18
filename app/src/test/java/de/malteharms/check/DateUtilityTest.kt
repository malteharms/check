package de.malteharms.check

import de.malteharms.check.pages.reminder.data.calculateCorrectYearOfNextBirthday
import de.malteharms.check.pages.reminder.data.timeBetween
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class DateUtilityTest {

    @Test
    fun daysBetween5_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5).atStartOfDay()
        val dateInPast = LocalDate.of(2024, 5, 10).atStartOfDay()

        val expectedRange = 5L
        val actualRange = timeBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange.days)
    }

    @Test
    fun daysBetween0_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5).atStartOfDay()
        val dateInPast = LocalDate.of(2024, 5, 5).atStartOfDay()

        val expectedRange = 0L
        val actualRange = timeBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange.days)
    }

    @Test
    fun daysBetween1_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5).atStartOfDay()
        val dateInPast = LocalDate.of(2024, 5, 6).atStartOfDay()

        val expectedRange = 1L
        val actualRange = timeBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange.days)
    }

    @Test
    fun daysBetween2_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5).atStartOfDay()
        val dateInPast = LocalDate.of(2024, 5, 7).atStartOfDay()

        val expectedRange = 2L
        val actualRange = timeBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange.days)
    }

    @Test
    fun daysBetweenM5_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 6).atStartOfDay()
        val dateInPast = LocalDate.of(2024, 5, 1).atStartOfDay()

        val expectedRange = -5L
        val actualRange = timeBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange.days)
    }

    @Test
    fun daysBetweenBig_isCorrect() {

        val currentDate = LocalDate.of(2024, 5, 5).atStartOfDay()
        val dayInPast = LocalDate.of(2028, 6, 28).atStartOfDay()

        val expectedRange = 1515L
        val actualRange = timeBetween(
            dateToReach = dayInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange.days)
    }

    @Test
    fun correctYearOfNextBirthdayBeforeToday_isCorrect() {
        val currentDate: LocalDateTime = LocalDate.of(2024, 5, 18).atStartOfDay()
        val beforeToday: LocalDateTime = LocalDate.of(2024, 4, 18).atStartOfDay()

        val expectedYear: Int = 2025
        val actualYear: Int = calculateCorrectYearOfNextBirthday(
            dateToReview = beforeToday,
            today = currentDate
        )

        Assert.assertEquals(expectedYear, actualYear)
    }

    @Test
    fun correctYearOfNextBirthdayAfterToday_isCorrect() {
        val currentDate: LocalDateTime = LocalDate.of(2024, 5, 18).atStartOfDay()
        val beforeToday: LocalDateTime = LocalDate.of(2024, 6, 18).atStartOfDay()

        val expectedYear: Int = 2024
        val actualYear: Int = calculateCorrectYearOfNextBirthday(
            dateToReview = beforeToday,
            today = currentDate
        )

        Assert.assertEquals(expectedYear, actualYear)
    }

}
