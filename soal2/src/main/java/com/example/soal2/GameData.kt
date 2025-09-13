package com.example.soal2

data class Wizard(
    var name: String = "Wizard",
    var maxHp: Int = 50,
    var currentHp: Int = 50,
    var maxMana: Int = 30,
    var currentMana: Int = 30,
    var kills: Int = 0,
    var killsToEvolve: Int = 5,
    var healthPotions: Int = 5,
    var manaPotions: Int = 5,
    var isEvolved: Boolean = false,
    var lifesteal: Int = 0
)

data class Enemy(
    val type: String,
    val maxHp: Int,
    var currentHp: Int,
    val damage: Int = 10
)

enum class SpellType {
    FIRE, WATER, GRASS
}