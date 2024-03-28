package de.malteharms.check.pages.food.data


const val FOOD_CONTAINER_HEIGHT = 40
const val FOOD_CONTAINER_BOTTOM_SPACING = 15

const val FOOD_BOX_WIDTH_IN_PERCENT = 0.5
const val FOOD_BOX_PADDING_END = 20
const val FOOD_BOX_CORNER_RADIUS = 10

fun getWeekDays(): List<String> {
    return listOf(
        "Montag",
        "Dienstag",
        "Mittwoch",
        "Donnerstag",
        "Freitag",
        "Samstag",
        "Sonntag"
    )
}
