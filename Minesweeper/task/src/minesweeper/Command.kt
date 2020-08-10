package minesweeper

import java.security.InvalidParameterException
import java.util.*

class Command(sc: Scanner) {
    val col: Int
    val row: Int
    val isReveal: Boolean

    init {
        col = if (sc.hasNextInt()) {
            sc.nextInt()
        } else {
            throw InvalidParameterException("x must be integer number! ")
        }
        row = if (sc.hasNextInt()) {
            sc.nextInt()
        } else {
            throw InvalidParameterException("y must be integer number! ")
        }
        isReveal = when (sc.nextLine().trim().toLowerCase()) {
            "free" -> true
            "mine" -> false
            else -> throw InvalidParameterException("Choose 'free' or 'mine' as third argument!")
        }

    }

}
