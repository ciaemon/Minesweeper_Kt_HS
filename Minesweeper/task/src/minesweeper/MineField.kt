package minesweeper

import java.security.InvalidParameterException

/**
 * Class that represents playfield. The class  has following public fields:
 * val sizeX: Int
 * val sizeY: Int - sizes of the playfield
 * val minesAtStart: Int - amount of mines on the playfield
 *
 */
class MineField(val sizeX: Int, val sizeY: Int, mines: Int) : Iterable<MineField.Cell> {
    val minesAtStart: Int = if (mines in 0..sizeX * sizeY) mines else sizeX * sizeY - 1

    private val mineField: Array<Array<Cell>>


    init {
        mineField = Array(sizeX) {i -> Array(sizeY) {j -> Cell( i + 1, j + 1, false) } }
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
                    getCell(i, j).isMine = false
                    continue // starting cell
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
        calculateAdjacent()
    }

     /**
     * Calculates adjacent mines value for all cells
     */
     private fun calculateAdjacent() {

        for (cell in this) {
            var neighbours = 0
            for (adjCell in cell) {
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
        return if (x in 1..sizeX && y in 1..sizeY) mineField[x - 1][y - 1]
        else Cell(x, y, false)

    }

    /**
     * Replace cell in minefield
     */
    fun setCell(cell: Cell) {
        mineField[cell.x - 1][cell.y - 1] = cell
        calculateAdjacent()
    }

    /**
     * Calculates if new cell should contain a mine
     */
    private fun isAdd(minesRemains: Int, cellsRemains: Int) = (minesRemains.toDouble() / cellsRemains) > Math.random()

    /**
     * Prints current playfield. You can choose print hidden cells as well
     */
    fun print(showHidden: Boolean = false) {

        print(" |")
        for (i in 1..sizeX) {
            print(i)
        }
        println("|")
        println("—│${"-".repeat(sizeX)}│")
        for (cell in this) {
            if (cell.x == 1) {
                print("${cell.y}|")
            }
            cell.print(showHidden)
            if (cell.x == sizeX) {
                println("|")
            }

        }
        println("—│${"-".repeat(sizeX)}│")
    }

    /**
     * Returns an iterator over all cells in minefield.
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
    private fun checkWinningCondition(winCondition: (checkedCell: Cell) -> Boolean): Boolean {
        for (cell in this) {
            if (!winCondition(cell)) return false
        }
        return true
    }

    /**
     * Class Cell represents cell in Minesweeper playfield. It has several fields
     * val x: Int - row number (from 1 to sizeX)
     * val y: Int - column number (from 1 to sizeY)
     * var isMine: Boolean - true if cell contains a mine
     * var isVisible: Boolean - true if cell is visible and not hidden
     * var isMarked: Boolean - true if cell is marked as a mine. Only hidden cells can be marked
     * var adjacentMines: Int - number of mines adjacent to this cell. Value is -1 for cells with mines
     */
    inner class Cell (val x: Int, val y: Int, var isMine: Boolean) : Iterable<MineField.Cell> {
        var isVisible = false
        var isMarked = false
            set(value) {
                field = if (isVisible) false else value // Only hidden cells can be marked
            }
        var adjacentMines = 0
            set(value) { field = when {
                isMine -> -1 // adjacentMines is -1 for cells with mines
                value in 0..8 -> value
                else -> 0 // default value if out of bound
            }
            }

        /**
         * Prints character for this cell
         */
        fun print(showHidden: Boolean) {
            print(when {
                isMarked && !isVisible-> '*' // marked mine symbol
                isMine && (showHidden || isVisible) -> 'X' // mine symbol
                adjacentMines > 0 && (showHidden || isVisible) -> adjacentMines // number of adjacent mines
                isVisible && adjacentMines == 0 -> '/' // if cell have no adjacent mines
                else -> '.' // hidden and unmarked cell
            })
        }

        fun changeMark() {
            isMarked = !isMarked
        }

        /**
         * Reveals cell and return false if it contains mine
         */
        fun reveal(): Boolean {
            if (isMine) {
                return false
            }

            if (x in 1..sizeX && y in 1..sizeY) {
                isVisible = true
                // If revealed cell has 0 adjacent mines, all adjacent cells will be also revealed
                if (adjacentMines == 0) {
                    for (adjCell in this) {
                        if (!adjCell.isVisible) {
                            adjCell.reveal()
                        }
                    }
                }
            } else return true
            return true
        }

        /**
         * Returns an iterator over the adjacent cells of this cell.
         */
        override fun iterator(): Iterator<Cell> = AdjacentCellsIterator(this)

    }
    
    /**
     * Iterator for cell over all adjacent cells
     */
    inner class AdjacentCellsIterator(private val cell: Cell) : Iterator<Cell> {


        private val vectorsIterator = arrayOf( // Eight vectors from origin to adjacent cells
                Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
                Pair(0, -1), Pair(0, 1),
                Pair(1, -1), Pair(1, 0), Pair(1, 1))
                .iterator()
        /**
         * Returns `true` if the iteration has more elements.
         */
        override fun hasNext(): Boolean = vectorsIterator.hasNext()


        /**
         * Returns the next element in the iteration.
         */
        override fun next(): Cell {
            val currentVector = vectorsIterator.next()
            return getCell(cell.x + currentVector.first, cell.y + currentVector.second)
        }

    }

    /**
     * Executes Command object over the minefield.
     */
    fun execute(command: Command): Boolean {
        val x = command.col
        val y = command.row
        if (x !in 1..sizeX) {
            throw InvalidParameterException("x value is out of range!")
        }
        if (y !in 1..sizeY) {
            throw InvalidParameterException("y value is out of range!")
        }
        if (command.isReveal) {
            return getCell(x, y).reveal()

        } else {
            getCell(x, y).changeMark()
        }
        return true
    }
}