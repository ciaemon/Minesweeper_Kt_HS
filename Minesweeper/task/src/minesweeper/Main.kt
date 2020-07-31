package minesweeper

import java.util.*
import kotlin.random.Random

fun create(): Char {
    val random = Random.Default;
    return if (random.nextBoolean()) '.' else 'X'
}
fun main() {

    val sc = Scanner(System.`in`)
    val sizeX = 5 //sc.nextInt()
    val sizeY = 5 //sc.nextInt()
    for (i in 1..sizeX) {
        for (j in 1..sizeY) {
            print(create())
        }
        println()
    }

}
