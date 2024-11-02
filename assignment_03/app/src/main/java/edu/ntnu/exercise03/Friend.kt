package edu.ntnu.exercise03
import java.io.Serializable

data class Friend(
    val name: String,
    val birthdate: String
) : Serializable
