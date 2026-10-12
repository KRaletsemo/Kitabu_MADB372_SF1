package com.example.kitabusf1.domain
//File to handle validation
const val MAX_SEARCH_LENGTH = 50

fun sanitiseSearchText(input: String): String =
    input.filterNot { it.isISOControl() }.take(MAX_SEARCH_LENGTH)
//FilterNot removes invisible char's
//Uses a max length to limit characters