package minesweeper

import java.util.*

fun main() {
    val sc = Scanner(System.`in`)
    var message: String = ""
    println("How many mines do you want on the field?")
    val mines = sc.nextInt()
    val f = MineField(9, 9, mines)
    f.print()
    var firstMove = true
    while (!f.checkWin()) {
        print("Set/unset mine marks or claim a cell as free: ")
        val x = sc.nextInt()
        val y = sc.nextInt()
        val toMine = sc.nextLine() == "mine"
        if (!(x in 1..f.sizeX || y in 1..f.sizeY)) {
            println("Incorrect coordinates")
            continue
        }
        if (firstMove && !toMine) {
            f.renew(y - 1, x - 1)
            f.calculateAdjacent()
            firstMove = false
        }
        val currentCell = f.getCell(y - 1, x - 1)
        if (toMine) {
            currentCell.changeMark()
        } else {
            if (!f.reveal(y - 1, x - 1)) {
                message = "You stepped on a mine and failed!"
                break
            }
        }
        f.print()
    }


}
