package com.chronosoft.sheepwolf.game.run

import com.chronosoft.sheepwolf.game.model.event.Move
import java.util.function.Consumer

data class SheepWolf(
    private val onMove: Consumer<Move> = Consumer { },
    private val sheeps: MutableSet<String> = mutableSetOf("a1", "c1", "e1", "g1"),
    private var wolf: String = "d8",
    private var turn: Char = 'w'
) {
    private fun neighbourLetters(letter: Char): List<Char> {
        return listOf(letter - 1, letter + 1).filter { it in 'a'..'h' }
    }

    private fun neighbourNumbers(number: Char): List<Char> {
        return listOf(number - 1, number + 1).filter { it in '1'..'8' }
    }

    fun sheeps(): List<String> {
        return sheeps.toList()
    }

    fun wolf(): String {
        return "" + wolf
    }

    fun turn(): Char {
        return turn
    }

    fun possibleMoves(from: String): Set<Move> {
        val fromLetter = from[0]
        val fromNumber = from[1]
        val isWolf = from == wolf
        val toLetters = neighbourLetters(fromLetter)
        val toNumbers = neighbourNumbers(fromNumber).filter { isWolf || it > fromNumber }
        return toNumbers
            .flatMap { num -> toLetters.map { "" + it + num } }
            .filter { it != wolf && it !in sheeps }
            .map { Move(from, it) }
            .toSet()
    }

    fun wolfWon(): Boolean {
        val wolfRange = wolf[1]
        val minSheepRange = sheeps.minOf { it[1] }
        return wolfRange <= minSheepRange
    }

    fun sheepsWon(): Boolean {
        return possibleMoves(wolf).isEmpty()
    }

    fun gameOver(): Boolean {
        return wolfWon() || sheepsWon()
    }

    fun applyMove(move: Move) {
        if (turn == 'b' && move.from == wolf) {
            wolf = move.to
        } else if (turn == 'w' && move.from in sheeps) {
            sheeps.remove(move.from)
            sheeps.add(move.to)
        } else {
            throw IllegalArgumentException("Invalid move")
        }
        turn = if (gameOver()) ' ' else if (turn == 'w') 'b' else 'w'
        onMove.accept(move)
    }
}
