package de.malteharms.utils.logic

import de.malteharms.utils.model.DateExt
import de.malteharms.utils.model.TimePeriod
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


/**
 * Calculate the [TimePeriod] between two dates.
 *
 * @param end The timestamp which is interpreted as the end of the time period.
 * @param start The timestamp which is interpreted as the start of the time period. The default is the current [LocalDateTime].
 * @return The [TimePeriod] between the provided timestamps.
 *
 * @author Malte Harms
 */
fun timeBetween(end: DateExt, start: DateExt = DateExt.now()): TimePeriod {
    return TimePeriod(
        days = start.until(end, ChronoUnit.DAYS),
        months = start.until(end, ChronoUnit.MONTHS),
        years = start.until(end, ChronoUnit.YEARS),
    )
}
