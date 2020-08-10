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
        return y <= mineField.sizeY
    }

    /**
     * Returns the next element in the iteration.
     */
    override fun next(): MineField.Cell {
        val cell = mineField.getCell(x, y)
        if (x != mineField.sizeX) {
            x++
        } else {
            x = 1
            y++
        }
        return cell
    }

}