package minesweeper

/**
 * Class Cell represents cell in Minesweeper playfield. It has several fields
 * val x: Int - column number (from 1 to sizeX)
 * val y: Int - row number (from 1 to sizeY)
 * var isMine: boolean - check if cell contains a mine
 * var isVisible: Boolean - checks if cell is visible and not hidden
 * var isMarked: Boolean - checks if cell is marked as a mine. Only hidden cells can be marked
 * var neighbours: Int - number of mines adjacent to this cell. Neighbours is -1 for cells with mines
 */
class Cell (val x: Int, val y: Int, var isMine: Boolean) {
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

}