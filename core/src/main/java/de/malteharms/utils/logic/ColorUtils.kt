package de.malteharms.utils.logic

import kotlin.random.Random

fun getRandomHexString(): String {
    val random = Random(System.currentTimeMillis())
    val rgb =  "#" + "%06X".format(random.nextInt(0xFFFFFF + 1))
    return rgb
}
