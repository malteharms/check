package de.malteharms.utils.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateExt (
    private val date: LocalDateTime
){

    companion object {
        fun now(atStartOfDay: Boolean = true): DateExt {
            val date = if (atStartOfDay) {
                LocalDate.now().atStartOfDay()
            } else LocalDateTime.now()

            return DateExt(date)
        }

        fun from(timestamp: Long): DateExt {
            val d = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
            return DateExt(d)
        }

        fun from(year: Int, month: Int, day: Int): DateExt {
            return DateExt(LocalDate.of(year, month, day).atStartOfDay())
        }
    }

    override fun toString(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(formatter)
    }

    // ===== overwritten functions =====
    fun get(): DateExt {
        return this
    }

    fun toTimestamp(): Long {
        return date.atZone(ZoneId.systemDefault()).toEpochSecond()
    }

    fun until(end: DateExt, unit: ChronoUnit): Long {
        return date.until(end.date, unit)
    }

    fun plus(value: Int, unit: ChronoUnit): DateExt {
        val updatedDate = when(unit) {
            ChronoUnit.DAYS -> date.plusDays(value.toLong())
            ChronoUnit.MONTHS -> date.plusMonths(value.toLong())
            ChronoUnit.YEARS -> date.plusYears(value.toLong())
            else -> throw UnsupportedOperationException("Not implemented")
        }

        return DateExt(updatedDate)
    }

    /**
     * Checks if the given date has already passed.
     *
     * @param referenceDate The date which should be used for the reference (default: current date).
     * @param applyCurrentYear If set, [date] will get applied the year of the current date.
     * @return If true, the day is laying in the past.
     * @author Malte Harms
     */
    fun hasPassed(
        referenceDate: LocalDateTime = LocalDate.now().atStartOfDay(),
        applyCurrentYear: Boolean = true
    ): Boolean {
        val adjustedYear: Int = if (applyCurrentYear) {
            LocalDate.now().year
        } else date.year

        return date.withYear(adjustedYear).isBefore(referenceDate)
    }

    /**
     * Updates the object with the next year from [reference].
     *
     * @param reference The reference to check against.
     * @return The may updated [DateExt] object.
     * @author Malte Harms
     */
    fun applyNextYear(
        reference: DateExt = now()
    ): DateExt {
        if (this.hasPassed()) {
            return DateExt(date.withYear(reference.date.year + 1))
        }

        return this
    }

    /**
     * Subtracts a given value from the due date based on the specified time unit.
     *
     * @param value The value to subtract.
     * @param timeUnit The time unit for the subtraction.
     * @return A new [LocalDateTime] instance representing the due date after the subtraction.
     *
     * @author Malte Harms
     */
    fun subtractTimeUnit(
        value: Int,
        timeUnit: ChronoUnit
    ): DateExt {
        val adjustedDate: LocalDateTime = when (timeUnit) {
            ChronoUnit.DAYS -> date.minusDays(value.toLong())
            ChronoUnit.MONTHS -> date.minusMonths(value.toLong())
            else -> throw UnsupportedOperationException("Wrong time unit parsed")
        }

        return DateExt(adjustedDate)
    }

}
