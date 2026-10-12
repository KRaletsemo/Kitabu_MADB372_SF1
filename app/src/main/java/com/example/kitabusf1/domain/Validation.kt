package com.example.kitabusf1.domain
//File to handle validation
const val MAX_SEARCH_LENGTH = 50

fun sanitiseSearchText(input: String): String =
    input.filterNot { it.isISOControl() }.take(MAX_SEARCH_LENGTH)
//FilterNot removes invisible char's
//Uses a max length to limit characters

//Ensures the rental time is only one of the mentioned number
val RENTAL_OPTIONS = listOf(7, 14, 21)

fun isValidRentalDays(days: Int): Boolean = days in RENTAL_OPTIONS
