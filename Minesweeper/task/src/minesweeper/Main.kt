package minesweeper

import java.util.*

class Cell (val x: Int, val y: Int, var isMine: Boolean) {
    var isVisible = false
    var neighbors = 0
        set(value) { field = when {
            isMine -> -1
            value in 0..8 -> value
            else -> {
                isMine = true
                -1
            }
        }
        }
    fun print(showHidden: Boolean) {
        print(when {
            isMine && (showHidden || isVisible) -> 'X'
            !isMine && (showHidden || isVisible) && neighbors != 0 -> neighbors
            !showHidden && !isVisible -> '.'
            else -> '.'
        })
    }

}

class MineField {
    val sizeX: Int
    val sizeY: Int
    val minesAtStart: Int

    private val mineField: Array<Array<Cell>>

    constructor(sizeX: Int, sizeY: Int, mines: Int) {
        this.sizeX = sizeX
        this.sizeY = sizeY
        this.minesAtStart = mines
        mineField = Array(sizeX) {i -> Array(sizeY) {j -> Cell(i, j, false)} }
        renew()
        calculateNeighbours()
    }
    fun renew() {
        var mines = minesAtStart
        var cells = sizeX * sizeY
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                if (isAdd(mines, cells)) {
                    mines--
                    mineField[i][j].isMine = true
                } else {
                    mineField[i][j].isMine = false
                }
                cells--
            }
        }
    }

    fun calculateNeighbours() {

        val vectors =
                arrayOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
                        Pair(0, -1), Pair(0, 1),
                        Pair(1, -1), Pair(1, 0), Pair(1, 1))

        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                var neighbours = 0;
                for (pair in vectors) {
                    if (getCell(i + pair.first, j + pair.second).isMine) neighbours++
                }
                getCell(i, j).neighbors = neighbours

            }
        }

    }

    fun getCell(x: Int, y: Int): Cell {
        return if (x in 0 until sizeX && y in 0 until sizeY) mineField[x][y]
        else Cell(x, y, false)

    }
    private fun isAdd(minesRemains: Int, cellsRemains: Int) = (minesRemains.toDouble() / cellsRemains) > Math.random()

    fun print(showHidden: Boolean) {
        for (row in mineField) {
            for (cell in row) {
                cell.print(showHidden)
            }
            println()
        }
    }


}

fun main() {
    val sc = Scanner(System.`in`)
    println("How many mines do you want on the field?")
    val mines = sc.nextInt()
    val f = MineField(9, 9, mines)
    f.print(true)



}
