package com.chronosoft.sheepwolf.game

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SheepwolfGameApplication

fun main(args: Array<String>) {
    runApplication<SheepwolfGameApplication>(*args)
}
