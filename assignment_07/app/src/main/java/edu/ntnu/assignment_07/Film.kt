/**
 * src/main/java/edu/ntnu/assignment_07/Film
 */

package edu.ntnu.assignment_07

/**
 * Android/Kotlin har Movie som et reservert klassenavn, så bruker Film i stedet.
 */
data class Film(
    val title: String,
    val director: String,
    val actors: List<String>
)

// End of class Film