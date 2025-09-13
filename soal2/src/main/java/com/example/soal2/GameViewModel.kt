package com.example.soal2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val _wizard = MutableStateFlow(Wizard())
    private val _currentEnemy = MutableStateFlow<Enemy?>(null)
    private val _gameState = MutableStateFlow("name_input")
    private val _message = MutableStateFlow("")

    val wizard: StateFlow<Wizard> = _wizard
    val currentEnemy: StateFlow<Enemy?> = _currentEnemy
    val gameState: StateFlow<String> = _gameState
    val message: StateFlow<String> = _message

    fun setWizardName(name: String) {
        if (name.isBlank()) {
            _message.value = "Name cannot be empty!"
            return
        }
        _wizard.value = _wizard.value.copy(name = name)
        _gameState.value = "main_menu"
        _message.value = "Good luck, $name! You're gonna need it!"
    }

    fun startBattle() {
        val enemyType = listOf("Fire", "Water", "Grass").random()
        val enemyHp = Random.nextInt(30, 51)
        _currentEnemy.value = Enemy(
            type = enemyType,
            maxHp = enemyHp,
            currentHp = enemyHp
        )
        _gameState.value = "battle"
        _message.value = "A ${enemyType}mon appeared!"
    }

    fun castSpell(spellType: SpellType) {
        val wizardValue = _wizard.value
        val enemyValue = _currentEnemy.value

        if (wizardValue.currentMana < 10) {
            _message.value = "Not enough mana!"
            return
        }

        if (enemyValue == null) {
            _message.value = "No enemy to attack!"
            return
        }

        // Calculate damage
        var damage = 10
        val effectiveness = when {
            spellType == SpellType.FIRE && enemyValue.type == "Grass" -> 2.0
            spellType == SpellType.WATER && enemyValue.type == "Fire" -> 2.0
            spellType == SpellType.GRASS && enemyValue.type == "Water" -> 2.0
            else -> 1.0
        }

        if (wizardValue.isEvolved) {
            damage = (damage * 1.5).toInt()
        }

        val finalDamage = (damage * effectiveness).toInt()
        enemyValue.currentHp -= finalDamage

        // Apply lifesteal if evolved
        if (wizardValue.isEvolved && wizardValue.lifesteal > 0) {
            val healAmount = wizardValue.lifesteal
            wizardValue.currentHp = minOf(wizardValue.currentHp + healAmount, wizardValue.maxHp)
        }

        wizardValue.currentMana -= 10

        _message.value = "You dealt $finalDamage damage with ${spellType.name.lowercase()} spell!"

        // Check if enemy is defeated
        if (enemyValue.currentHp <= 0) {
            wizardValue.kills++
            if (wizardValue.isEvolved) {
                wizardValue.lifesteal++
            }
            _message.value = "You defeated the ${enemyValue.type}mon! +1 kill"
            checkEvolution()
            _gameState.value = "main_menu"
            _currentEnemy.value = null
        } else {
            // Enemy attacks back
            wizardValue.currentHp -= enemyValue.damage
            _message.value += "\n${enemyValue.type}mon attacked you for ${enemyValue.damage} damage!"

            // Check if wizard died
            if (wizardValue.currentHp <= 0) {
                _message.value = "You died! Game over."
                resetGame()
            }
        }

        _wizard.value = wizardValue
    }

    fun drinkHealthPotion() {
        val wizardValue = _wizard.value
        if (wizardValue.healthPotions > 0) {
            wizardValue.currentHp = minOf(wizardValue.currentHp + 25, wizardValue.maxHp)
            wizardValue.healthPotions--
            _message.value = "Drank health potion! +25 HP"
        } else {
            _message.value = "No health potions left!"
        }
        _wizard.value = wizardValue
    }

    fun drinkManaPotion() {
        val wizardValue = _wizard.value
        if (wizardValue.manaPotions > 0) {
            wizardValue.currentMana = minOf(wizardValue.currentMana + 15, wizardValue.maxMana)
            wizardValue.manaPotions--
            _message.value = "Drank mana potion! +15 MP"
        } else {
            _message.value = "No mana potions left!"
        }
        _wizard.value = wizardValue
    }

    fun runFromBattle() {
        _message.value = "You ran away safely!"
        _gameState.value = "main_menu"
        _currentEnemy.value = null
    }

    private fun checkEvolution() {
        val wizardValue = _wizard.value
        if (!wizardValue.isEvolved && wizardValue.kills >= wizardValue.killsToEvolve) {
            wizardValue.isEvolved = true
            wizardValue.maxHp = (wizardValue.maxHp * 1.5).toInt()
            wizardValue.currentHp = wizardValue.maxHp
            wizardValue.maxMana = (wizardValue.maxMana * 1.5).toInt()
            wizardValue.currentMana = wizardValue.maxMana
            wizardValue.lifesteal = 1
            _message.value = "You evolved into a Strong Wizard! Gained lifesteal ability!"
        }
        _wizard.value = wizardValue
    }

    fun resetGame() {
        _wizard.value = Wizard(name = _wizard.value.name)
        _currentEnemy.value = null
        _gameState.value = "main_menu"
    }

    fun backToMainMenu() {
        _gameState.value = "main_menu"
    }

    fun renameWizard(newName: String) {
        if (newName.isBlank()) {
            _message.value = "Name cannot be empty!"
            return
        }
        _wizard.value = _wizard.value.copy(name = newName)
        _message.value = "Name changed to $newName"
    }
}