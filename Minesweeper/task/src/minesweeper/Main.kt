package minesweeper

import java.security.InvalidParameterException
import java.util.*

fun main() {
    val sc = Scanner(System.`in`)
    val f = MineField(sizeX = 9, sizeY = 9, mines = sc.nextInt())
    var isFirstMove = true
    var message = ""
    while (!f.checkWin()) {
        try {
           if(!f.execute(Command(sc))) {
               println("You stepped on the mine and failed!")
           }
        } catch (e: InvalidParameterException) {
            println(e.message)
        }
    }

}
