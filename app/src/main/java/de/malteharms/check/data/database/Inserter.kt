package de.malteharms.check.data.database

import de.malteharms.check.data.database.tables.Birthday
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.domain.CheckDao

suspend fun insertReminderItemForBirthday(dao: CheckDao, item: Birthday) {

    dao.insertReminderItem(ReminderItem(
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.applyNextYear()
    ))
}

suspend fun updateReminderItemForBirthday(dao: CheckDao, reminderId: Long, item: Birthday) {
    dao.updateReminderItem(ReminderItem(
        id = reminderId,
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.applyNextYear()
    ))
}
