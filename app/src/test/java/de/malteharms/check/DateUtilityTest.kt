package de.malteharms.check

import de.malteharms.check.pages.reminder.domain.convertLocalDateToTimestamp
import de.malteharms.check.pages.reminder.domain.convertTimestampToLocalDate
import de.malteharms.check.pages.reminder.domain.daysBetween
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class DateUtilityTest {

    @Test
    fun daysBetween5_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5)
        val dateInPast = LocalDate.of(2024, 5, 10)

        val expectedRange = 5
        val actualRange = daysBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange)
    }

    @Test
    fun daysBetween0_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5)
        val dateInPast = LocalDate.of(2024, 5, 5)

        val expectedRange = 0
        val actualRange = daysBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange)
    }

    @Test
    fun daysBetweenM5_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 6)
        val dateInPast = LocalDate.of(2024, 5, 1)

        val expectedRange = -5
        val actualRange = daysBetween(
            dateToReach = dateInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange)
    }

    @Test
    fun daysBetweenBig_isCorrect() {

        val currentDate = LocalDate.of(2024, 5, 5)
        val dayInPast = LocalDate.of(2028, 6, 28)

        val expectedRange = 1516
        val actualRange = daysBetween(
            dateToReach = dayInPast,
            today = currentDate
        )

        Assert.assertEquals(expectedRange, actualRange)
    }

}
