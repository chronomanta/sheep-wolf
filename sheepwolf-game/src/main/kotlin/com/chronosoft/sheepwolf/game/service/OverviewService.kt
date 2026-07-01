package com.chronosoft.sheepwolf.game.service

import com.chronosoft.sheepwolf.game.model.GamesOverview
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
data class OverviewService (
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    private val playersSessions: MutableSet<String> = ConcurrentHashMap.newKeySet()

    fun playerEnters(sessionId: String) = broadcastPlayerCount(playersSessions.add(sessionId).let { playersSessions.size })

    fun playerLeaves(sessionId: String) = broadcastPlayerCount(playersSessions.remove(sessionId).let { playersSessions.size })

    fun gamesOverview(): GamesOverview = GamesOverview(playersSessions.size)

    private fun broadcastPlayerCount(count: Int) = simpMessagingTemplate.convertAndSend("/topic/players-count", count)
}