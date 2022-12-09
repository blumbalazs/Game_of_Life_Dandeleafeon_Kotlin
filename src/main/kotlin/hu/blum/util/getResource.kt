package hu.blum.util

import hu.blum.Game

fun getResource(filename: String): String {
    return Game::class.java.getResource(filename)!!.toString()
}