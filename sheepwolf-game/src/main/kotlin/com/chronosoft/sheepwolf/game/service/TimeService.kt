package com.chronosoft.sheepwolf.game.service

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TimeService {
    fun now() = LocalDateTime.now()
}