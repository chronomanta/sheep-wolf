package com.chronosoft.sheepwolf.game.service

import com.chronosoft.sheepwolf.game.model.GamesOverview
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
data class OverviewService (
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    private val playersCount: AtomicInteger = AtomicInteger(0)

    fun playerEnters() = broadcastPlayerCount(playersCount.incrementAndGet())

    fun playerLeave() = broadcastPlayerCount(playersCount.decrementAndGet())

    fun gamesOverview(): GamesOverview = GamesOverview(playersCount.get())

    private fun broadcastPlayerCount(count: Int) = simpMessagingTemplate.convertAndSend("/topic/players-count", count)
}