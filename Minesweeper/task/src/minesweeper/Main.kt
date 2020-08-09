package minesweeper

import java.security.InvalidParameterException
import java.util.*

fun main() {
    val sc = Scanner(System.`in`)
    println("How many mines do you want on the field?")
    val f = MineField(sizeX = 9, sizeY = 9, mines = sc.nextInt())
    var isFirstMove = true
    var isFail = false
    f.print()

    while (!f.checkWin() && !isFail) {
        try {
            print("Set/unset mine marks or claim a cell as free: ")
            val command = Command(sc)
            // If command is first reveal operation then target cell must be free
            if (command.isReveal && isFirstMove) {
                isFirstMove = false
                f.renew(command.col, command.row)
            }
            // execute returns false only if operation tried to reveal a mine
            if (f.execute(command)) {
                f.print()
            } else {
                isFail = true
                // making all mines visible
                for (cell in f) {
                    if (cell.isMine) {
                        cell.isVisible = true
                    }
                }
                break
            }
        } catch (e: Exception) {
            println(e.message)
            continue
        }
    }
    println(if (isFail) "You stepped on a mine and failed!"
            else "Congratulations! You found all the mines!")

}
