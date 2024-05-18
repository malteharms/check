package de.malteharms.check.data.database

import de.malteharms.check.data.database.tables.Birthday
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.pages.reminder.data.calculateCorrectYearOfNextBirthday
import java.time.LocalDateTime

suspend fun insertReminderItemForBirthday(dao: CheckDao, item: Birthday) {
    val nextYearOfBirthday: Int = calculateCorrectYearOfNextBirthday(item.birthday)

    dao.insertReminderItem(ReminderItem(
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.withYear(nextYearOfBirthday)
    ))
}

suspend fun updateReminderItemForBirthday(dao: CheckDao, reminderId: Long, item: Birthday) {
    val nextYearOfBirthday: Int = calculateCorrectYearOfNextBirthday(item.birthday)

    dao.updateReminderItem(ReminderItem(
        id = reminderId,
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.withYear(nextYearOfBirthday)
    ))
}
