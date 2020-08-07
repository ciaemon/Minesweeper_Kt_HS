package minesweeper

/**
 * Iterator for MineField class
 */
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