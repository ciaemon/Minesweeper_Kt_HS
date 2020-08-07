package minesweeper

/**
 * Class that represents playfield. The class  has following public fields:
 * val sizeX: Int
 * val sizeY: Int - sizes of the playfield
 * val minesAtStart: Int - amount of mines on the playfield
 */
class MineField : Iterable<Cell> {
    val sizeX: Int
    val sizeY: Int
    val minesAtStart: Int

    private val mineField: Array<Array<Cell>>

    private val vectors =
            arrayOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
                    Pair(0, -1), Pair(0, 1),
                    Pair(1, -1), Pair(1, 0), Pair(1, 1))

    constructor(sizeX: Int, sizeY: Int, mines: Int) {
        this.sizeX = sizeX
        this.sizeY = sizeY
        // in case of incorrect value minesAtStart is set to maximum possible value
        this.minesAtStart = if (mines in 0..sizeX * sizeY) mines else sizeX * sizeY - 1
        mineField = Array(sizeX) {i -> Array(sizeY) {j -> Cell(i, j, false) } }
        renew()
        calculateAdjacent()
    }
    /**
     * Shuffles mines on playfield. Cell with coordinates (x, y) will be free
     */
    fun renew(x: Int = -1, y: Int = -1) {
        var mines = minesAtStart
        var cells = sizeX * sizeY
        if (x in 0..sizeX && y in 0..sizeY) cells--
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                if (i == x && j == y) {
                    continue
                }
                if (isAdd(mines, cells)) {
                    mines--
                    mineField[i][j].isMine = true
                } else {
                    mineField[i][j].isMine = false
                }
                // resetting cell status
                mineField[i][j].isVisible = false
                cells--
            }
        }
    }

    /**
     * Calculates adjacent mines value for all cells
     */
    fun calculateAdjacent() {

        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                var neighbours = 0;
                for (pair in vectors) {
                    if (getCell(i + pair.first, j + pair.second).isMine) neighbours++
                }
                getCell(i, j).adjacentMines = neighbours

            }
        }

    }

    /**
     * return cell with coordinates (x, y)
     */
    fun getCell(x: Int, y: Int): Cell {
        return if (x in 0 until sizeX && y in 0 until sizeY) mineField[x][y]
        else Cell(x, y, false)

    }

    /**
     * Replace cell in minefield
     */
    fun setCell(cell: Cell) {
        mineField[cell.x][cell.y] = cell
    }

    /**
     * Reveals cell with coordinates x, y and returns false if cell contains mine
     */
    fun reveal(x: Int, y: Int): Boolean {

        if (mineField[x][y].isMine) {
            return false
        }
        if (x in 0 until sizeX && y in 0 until sizeY) {
            mineField[x][y].isVisible = true
            if (mineField[x][y].adjacentMines == 0) {
                for (pair in vectors) {
                    reveal(x + pair.first, y + pair.second)
                }
            }
        }
        return true
    }

    private fun isAdd(minesRemains: Int, cellsRemains: Int) = (minesRemains.toDouble() / cellsRemains) > Math.random()

    /**
     * Prints current playfield. You can choose print hidden cells as well
     */
    fun print(showHidden: Boolean = false) {

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

    /**
     * Checks two winning conditions in game. One condition is enough for victory
     */
    fun checkWin() =
            // All mines are marked and no free cells are marked
            checkWinningCondition { cell -> cell.isMarked == cell.isMine } ||
            // All free cells are revealed
            checkWinningCondition { cell -> cell.isMine || cell.isVisible }

    /**
     * Checks every cell for satisfying winning condition winCondition(checkedCell).
     * Returns true if all cells in minefield pass the check
     */
    fun checkWinningCondition(winCondition: (checkedCell: Cell) -> Boolean): Boolean {
        for (cell in this) {
            if (!winCondition(cell)) return false
        }
        return true
    }
}