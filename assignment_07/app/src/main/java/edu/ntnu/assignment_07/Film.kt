/**
 * Class: src/edu/ntnu/assignment_07/Film
 */
package edu.ntnu.assignment_07

/**
 * Android have Movie as a reserved class name, so we're using Film instead.
 */
data class Film(
    val title: String,
    val director: String,
    val actors: List<String>
)

