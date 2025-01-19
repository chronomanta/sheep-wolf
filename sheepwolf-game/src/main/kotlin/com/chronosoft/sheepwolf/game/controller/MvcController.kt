package com.chronosoft.sheepwolf.game.controller

import com.chronosoft.sheepwolf.game.service.GameService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
data class MvcController(
    val gameService: GameService
) {

    @GetMapping
    fun listGames(model: Model): String {
        model.addAttribute("games", gameService.getGamesDetails())
        return "games"
    }
}
