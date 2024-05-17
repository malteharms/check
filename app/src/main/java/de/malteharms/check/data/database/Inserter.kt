package de.malteharms.check.data.database

import de.malteharms.check.data.database.tables.Birthday
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.domain.CheckDao
import java.time.LocalDateTime

suspend fun insertReminderItemForBirthday(dao: CheckDao, item: Birthday) {

    val today: LocalDateTime = LocalDateTime.now()
    val thisYear: Int = today.year

    val nextYearOfBirthday: Int = if (item.birthday.withYear(thisYear).isBefore(today)) {
        thisYear
    } else thisYear + 1

    dao.insertReminderItem(ReminderItem(
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.withYear(nextYearOfBirthday)
    ))
}

suspend fun updateReminderItemForBirthday(dao: CheckDao, reminderId: Long, item: Birthday) {
    val today: LocalDateTime = LocalDateTime.now()
    val thisYear: Int = today.year

    val nextYearOfBirthday: Int = if (item.birthday.withYear(thisYear).isBefore(today)) {
        thisYear
    } else thisYear + 1

    dao.updateReminderItem(ReminderItem(
        id = reminderId,
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.withYear(nextYearOfBirthday)
    ))
}
