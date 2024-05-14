package de.malteharms.check.pages.settings.data

enum class SettingsGroup {
    GENERAL,
    CASH,
    FOOD,
    HOME,
    PROFILE,
    REMINDER,
    TODO;

    override fun toString(): String {
        return when (this) {
            GENERAL -> "Allgemein"
            CASH -> "Cash"
            FOOD -> "Food"
            HOME -> "Home"
            PROFILE -> "Profil"
            REMINDER -> "Reminder"
            TODO -> "Todo"
        }
    }
}
