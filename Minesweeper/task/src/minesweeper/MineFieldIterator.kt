package minesweeper

/**
 * Iterator for MineField class
 */
class MineFieldIterator(private val mineField: MineField) : Iterator<MineField.Cell>  {
    private var x = 1
    private var y = 1

    /**
     * Returns `true` if the iteration has more elements.
     */
    override fun hasNext(): Boolean {
        return x <= mineField.sizeX
    }

    /**
     * Returns the next element in the iteration.
     */
    override fun next(): MineField.Cell {
        val cell = mineField.getCell(x, y)
        if (y != mineField.sizeY) {
            y++
        } else {
            y = 1
            x++
        }
        return cell
    }

}