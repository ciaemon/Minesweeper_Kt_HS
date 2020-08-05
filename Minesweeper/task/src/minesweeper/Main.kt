package minesweeper

import java.util.*

class Cell (val x: Int, val y: Int, var isMine: Boolean) {
    var isVisible = false
    var isMarked = false
        set(value) {
            field = if (neighbors in 1..9) false else value
        }
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
    fun mark() {
        this.isMarked = true
    }
    fun unMark() {
        this.isMarked = false
    }
    fun print(showHidden: Boolean) {
        print(when {
            isMarked -> '*'
            isMine && (showHidden || isVisible) -> 'X'
            !isMine && (showHidden || isVisible) && neighbors != 0 -> neighbors
            !showHidden && !isVisible -> '.'
            else -> '.'
        })
    }

}

class MineField : Iterable<Cell> {
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

    fun setCell(cell: Cell) {
        mineField[cell.x][cell.y] = cell
    }
    private fun isAdd(minesRemains: Int, cellsRemains: Int) = (minesRemains.toDouble() / cellsRemains) > Math.random()

    fun print(showHidden: Boolean) {

        println(" │123456789│")
        println("—│—————————│")
        for (row in mineField) {
            print("${row[0].x + 1}|")
            for (cell in row) {
                cell.print(showHidden)
            }
            println("|")
        }
        println("—│—————————│")
    }

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<Cell> = MineFieldIterator(this)
}

class MineFieldIterator(private val mineField: MineField) : Iterator<Cell>  {
    private var x = 0
    private var y = 0

    /**
     * Returns `true` if the iteration has more elements.
     */
    override fun hasNext(): Boolean {
        return x != mineField.sizeX - 1 || y != mineField.sizeY - 1
    }

    /**
     * Returns the next element in the iteration.
     */
    override fun next(): Cell {
        val cell = mineField.getCell(x, y)
        if (x != mineField.sizeX - 1) {
            x++
        } else {
            x = 0
            y++
        }
        return cell
    }

}

fun main() {
    val sc = Scanner(System.`in`)
    println("How many mines do you want on the field?")
    val mines = sc.nextInt()
    val f = MineField(9, 9, mines)


    f.print(false)
    while (true) {
        print("Set/delete mines marks (x and y coordinates): ")
        val x = sc.nextInt()
        val y = sc.nextInt()
        val currentCell = f.getCell(y - 1, x - 1)
        if (currentCell.neighbors in 1..8) {
            println("There is a number here!")
        } else {
            currentCell.isMarked = !currentCell.isMarked
            f.print(false)
            var win = true
            for (cell in f) {
                if (cell.isMarked != cell.isMine) {
                    win = false
                }
            }
            if (win) break
        }
    }
    println("Congratulations! You found all the mines!")


}
