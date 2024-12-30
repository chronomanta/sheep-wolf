package com.chronosoft.sheepwolf.game.run

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SheepWolfTest {

    @ParameterizedTest
    @MethodSource("wolfPositions")
    fun `should return possible moves for wolf`(wolf: String, sheeps: MutableSet<String>, expectedFrom: Set<String>, expectedTo: Set<String>) {
        // given
        val game = SheepWolf(sheeps = sheeps, wolf = wolf)

        // when
        val moves = game.possibleMoves(wolf)

        // then
        assertThat(moves.map { it.from }).containsOnly(*expectedFrom.toTypedArray())
        assertThat(moves.map { it.to }).containsExactlyInAnyOrder(*expectedTo.toTypedArray())
    }

    @Test
    fun `should return possible moves for sheep free`() {
        // given
        val game = SheepWolf()

        // when

        // then
        assertThat(game.possibleMoves("a1").map { it.to }).containsExactlyInAnyOrder("b2")
        assertThat(game.possibleMoves("c1").map { it.to }).containsExactlyInAnyOrder("b2", "d2")
        assertThat(game.possibleMoves("e1").map { it.to }).containsExactlyInAnyOrder("d2", "f2")
        assertThat(game.possibleMoves("g1").map { it.to }).containsExactlyInAnyOrder("f2", "h2")
    }

    @Test
    fun `should return possible moves for sheep blocked`() {
        // given
        val game = SheepWolf(sheeps = mutableSetOf("b4", "c3", "e3", "f4"), wolf = "d4")

        // when

        // then
        assertThat(game.possibleMoves("b4").map { it.to }).containsExactlyInAnyOrder("a5", "c5")
        assertThat(game.possibleMoves("c3")).isEmpty()
        assertThat(game.possibleMoves("e3")).isEmpty()
        assertThat(game.possibleMoves("f4").map { it.to }).containsExactlyInAnyOrder("e5", "g5")
    }

    companion object {
        @JvmStatic
        fun wolfPositions(): Stream<Arguments> {
            val sheepsStart = mutableSetOf("a1", "c1", "e1", "g1")
            return Stream.of(
                Arguments.of("d8", sheepsStart, setOf("d8"), setOf("c7", "e7")),
                Arguments.of("f4", sheepsStart, setOf("f4"), setOf("e3", "g3", "e5", "g5")),
                Arguments.of("h8", sheepsStart, setOf("h8"), setOf("g7")),
                Arguments.of("a5", sheepsStart, setOf("a5"), setOf("b4", "b6")),
                Arguments.of("f6", mutableSetOf("b6", "e7", "e5", "g5"), setOf("f6"), setOf("g7")),
                Arguments.of("d8", mutableSetOf("a1", "c7", "e7", "g1"), setOf<String>(), setOf<String>())
            )
        }
    }
}