package minesweeper

/**
 * Class that represents playfield. The class  has following public fields:
 * val sizeX: Int
 * val sizeY: Int - sizes of the playfield
 * val minesAtStart: Int - amount of mines on the playfield
 *
 */
class MineField : Iterable<Cell> {
    val sizeX: Int
    val sizeY: Int
    val minesAtStart: Int

    private val mineField: Array<Array<Cell>>

    private val vectors = // useful for checking adjacent cells
            arrayOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
                    Pair(0, -1), Pair(0, 1),
                    Pair(1, -1), Pair(1, 0), Pair(1, 1))

    constructor(sizeX: Int, sizeY: Int, mines: Int) {
        this.sizeX = sizeX
        this.sizeY = sizeY
        // in case of incorrect value minesAtStart is set to maximum possible value
        this.minesAtStart = if (mines in 0..sizeX * sizeY) mines else sizeX * sizeY - 1
        mineField = Array(sizeX) {i -> Array(sizeY) {j -> Cell(j + 1, i + 1, false) } }
        renew()
        calculateAdjacent()
    }


    /**
     * Shuffles mines on playfield. Cell with coordinates (x, y) will be free.
     */
    fun renew(x: Int = 0, y: Int = 0) {
        var mines = minesAtStart // Current amount of mines to be distributed
        var cells = sizeX * sizeY // Remaining amount of cells

        if (x in 1..sizeX && y in 1..sizeY) {
            cells-- //subtracting starting move
        }

        for (i in 1..sizeX) {
            for (j in 1..sizeY) {

                if (i == x && j == y) {
                    continue // skipping starting cell
                }

                if (isAdd(mines, cells)) {
                    mines--
                    getCell(i, j).isMine = true
                } else {
                    getCell(i, j).isMine = false
                }
                getCell(i, j).isVisible = false // cell is invisible after renew
                cells--
            }
        }
    }

    private fun adjacentCells(x: Int, y: Int): Array<Cell> {
        val res = Array<Cell>(8) {_ -> Cell(0, 0, false)}
        var index = 0
        for (pair in vectors) {
            res[index] = getCell(x + pair.first, y + pair.second)
        }
        return res
    }

    /**
     * Calculates adjacent mines value for all cells
     */
    fun calculateAdjacent() {

        for (cell in this) {
            var neighbours = 0
            for (adjCell in adjacentCells(cell.x, cell.y)) {
                if (adjCell.isMine) {
                    neighbours++
                }
            }
            cell.adjacentMines = neighbours
        }


    }

    /**
     * return cell with coordinates (x, y). If coordinates are out of range return new free Cell with given coordinates
     */
    fun getCell(x: Int, y: Int): Cell {
        return if (x in 1..sizeX && y in 1..sizeY) mineField[y - 1][x - 1]
        else Cell(x, y, false)

    }

    /**
     * Replace cell in minefield
     */
    fun setCell(cell: Cell) {
        mineField[cell.y - 1][cell.x - 1] = cell
    }

    /**
     * Reveals cell with coordinates x, y and returns false if cell contains mine
     */
    fun reveal(x: Int, y: Int): Boolean {
        val cell = getCell(x, y)
        if (getCell(x, y).isMine) {
            return false
        }

        if (x in 1..sizeX && y in 1..sizeY) {
            cell.isVisible = true
            if (cell.adjacentMines == 0) {
                for (adjCell in adjacentCells(cell.x, cell.y)) {
                    reveal(adjCell.x, adjCell.y)
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
            print("${row[0].y}|")
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