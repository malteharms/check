package de.malteharms.check

import de.malteharms.check.pages.reminder.domain.convertLocalDateToTimestamp
import de.malteharms.check.pages.reminder.domain.convertTimestampToLocalDate
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class ConvertFunctionTest {

    @Test
    fun localDateToTimestamp_isCorrect() {
        val currentDate = LocalDate.of(2024, 5, 5)

        val expectedTimestamp = 1714860000L
        val actualTimestamp: Long = convertLocalDateToTimestamp(currentDate)

        Assert.assertEquals(expectedTimestamp, actualTimestamp)
    }

    @Test
    fun timestampToLocalDate_isCorrect() {
        val timestamp = 1714860000L

        val expectedLocalDate = LocalDate.of(2024, 5, 5)
        val actualLocalDate = convertTimestampToLocalDate(timestamp)

        Assert.assertEquals(
            expectedLocalDate.toString(),
            actualLocalDate.toString()
        )
    }

}