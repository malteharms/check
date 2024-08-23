package de.malteharms.database

import de.malteharms.database.tables.Birthday
import de.malteharms.database.tables.ReminderCategory
import de.malteharms.database.tables.ReminderItem

suspend fun insertReminderItemForBirthday(dao: CheckDao, item: Birthday) {

    dao.insertReminderItem(
        ReminderItem(
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.applyNextYear()
    )
    )
}

suspend fun updateReminderItemForBirthday(dao: CheckDao, reminderId: Long, item: Birthday) {
    dao.updateReminderItem(
        ReminderItem(
        id = reminderId,
        title = "${item.name}'s Geburtstag",
        category = ReminderCategory.BIRTHDAY,
        birthdayRelation = item.id,
        dueDate = item.birthday.applyNextYear()
    )
    )
}
