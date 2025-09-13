package com.example.soal2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

object GameState {
    var wizard by mutableStateOf(Wizard())
    var currentEnemy: Enemy? by mutableStateOf(null)
    var gameState by mutableStateOf("name_input")
    var message by mutableStateOf("")

    fun setWizardName(name: String) {
        if (name.isBlank()) {
            message = "Name cannot be empty!"
            return
        }
        wizard = wizard.copy(name = name)
        gameState = "main_menu"
        message = "Good luck, $name! You're gonna need it!"
    }

    fun startBattle() {
        val enemyType = listOf("Fire", "Water", "Grass").random()
        val enemyHp = Random.nextInt(30, 51)
        currentEnemy = Enemy(
            type = enemyType,
            maxHp = enemyHp,
            currentHp = enemyHp
        )
        gameState = "battle"
        message = "A ${enemyType}mon appeared!"
    }

    fun castSpell(spellType: SpellType) {
        if (wizard.currentMana < 10) {
            message = "Not enough mana!"
            return
        }

        val enemy = currentEnemy
        if (enemy == null) {
            message = "No enemy to attack!"
            return
        }

        // Calculate damage
        var damage = 10
        val effectiveness = when {
            spellType == SpellType.FIRE && enemy.type == "Grass" -> 2.0
            spellType == SpellType.WATER && enemy.type == "Fire" -> 2.0
            spellType == SpellType.GRASS && enemy.type == "Water" -> 2.0
            else -> 1.0
        }

        if (wizard.isEvolved) {
            damage = (damage * 1.5).toInt()
        }

        val finalDamage = (damage * effectiveness).toInt()
        enemy.currentHp -= finalDamage

        // Apply lifesteal if evolved
        if (wizard.isEvolved && wizard.lifesteal > 0) {
            val healAmount = wizard.lifesteal
            wizard = wizard.copy(currentHp = minOf(wizard.currentHp + healAmount, wizard.maxHp))
        }

        wizard = wizard.copy(currentMana = wizard.currentMana - 10)

        message = "You dealt $finalDamage damage with ${spellType.name.lowercase()} spell!"

        // Check if enemy is defeated
        if (enemy.currentHp <= 0) {
            wizard = wizard.copy(kills = wizard.kills + 1)
            if (wizard.isEvolved) {
                wizard = wizard.copy(lifesteal = wizard.lifesteal + 1)
            }
            message = "You defeated the ${enemy.type}mon! +1 kill"
            checkEvolution()
            gameState = "main_menu"
            currentEnemy = null
        } else {
            // Enemy attacks back
            wizard = wizard.copy(currentHp = wizard.currentHp - enemy.damage)
            message += "\n${enemy.type}mon attacked you for ${enemy.damage} damage!"

            // Check if wizard died
            if (wizard.currentHp <= 0) {
                message = "You died! Game over."
                resetGame()
            }
        }
    }

    fun drinkHealthPotion() {
        if (wizard.healthPotions > 0) {
            wizard = wizard.copy(
                currentHp = minOf(wizard.currentHp + 25, wizard.maxHp),
                healthPotions = wizard.healthPotions - 1
            )
            message = "Drank health potion! +25 HP"
        } else {
            message = "No health potions left!"
        }
    }

    fun drinkManaPotion() {
        if (wizard.manaPotions > 0) {
            wizard = wizard.copy(
                currentMana = minOf(wizard.currentMana + 15, wizard.maxMana),
                manaPotions = wizard.manaPotions - 1
            )
            message = "Drank mana potion! +15 MP"
        } else {
            message = "No mana potions left!"
        }
    }

    fun runFromBattle() {
        message = "You ran away safely!"
        gameState = "main_menu"
        currentEnemy = null
    }

    private fun checkEvolution() {
        if (!wizard.isEvolved && wizard.kills >= wizard.killsToEvolve) {
            wizard = wizard.copy(
                isEvolved = true,
                maxHp = (wizard.maxHp * 1.5).toInt(),
                currentHp = (wizard.maxHp * 1.5).toInt(),
                maxMana = (wizard.maxMana * 1.5).toInt(),
                currentMana = (wizard.maxMana * 1.5).toInt(),
                lifesteal = 1
            )
            message = "You evolved into a Strong Wizard! Gained lifesteal ability!"
        }
    }

    fun resetGame() {
        wizard = Wizard(name = wizard.name)
        currentEnemy = null
        gameState = "main_menu"
    }

    fun backToMainMenu() {
        gameState = "main_menu"
    }
}