package com.david0926.simpletodo

import java.io.Serializable

data class Todo(
    var text: String = "",
    var done: Boolean = false,
    var onEdit: Boolean = true
) : Serializable {

}