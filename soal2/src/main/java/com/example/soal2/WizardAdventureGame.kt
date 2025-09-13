package com.example.soal2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun WizardAdventureGame() {
    when (GameState.gameState) {
        "name_input" -> NameInputScreen()
        "main_menu" -> MainMenuScreen()
        "stats" -> StatsScreen()
        "battle" -> BattleScreen()
    }
}

@Composable
fun NameInputScreen() {
    var name by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("What's your name?", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { GameState.setWizardName(name.text) }) {
            Text("Start Adventure")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(GameState.message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun MainMenuScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, ${GameState.wizard.name}!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(GameState.message, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Text("What're you going to do?", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { GameState.gameState = "stats" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("1. View Stats")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { GameState.startBattle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("2. Enter Battle")
        }
    }
}

@Composable
fun StatsScreen() {
    val wizard = GameState.wizard

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("--- ${wizard.name}'s STATS ---", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("HP: ${wizard.currentHp}/${wizard.maxHp}")
        Text("Mana: ${wizard.currentMana}/${wizard.maxMana}")
        Text("Kills needed to evolve: ${wizard.kills}/${wizard.killsToEvolve}")
        Text("Mana Potions held: ${wizard.manaPotions}")
        Text("Health Potions held: ${wizard.healthPotions}")
        if (wizard.isEvolved) {
            Text("Lifesteal: ${wizard.lifesteal}")
            Text("Status: Strong Wizard")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(GameState.message)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { GameState.drinkManaPotion() }) {
            Text("a. Drink Mana Potion")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { GameState.drinkHealthPotion() }) {
            Text("b. Drink Health Potion")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { GameState.backToMainMenu() }) {
            Text("d. Back")
        }
    }
}

@Composable
fun BattleScreen() {
    val wizard = GameState.wizard
    val enemy = GameState.currentEnemy

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("--- BATTLE ---", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("${wizard.name}")
        Text("HP: ${wizard.currentHp}/${wizard.maxHp}")
        Text("Mana: ${wizard.currentMana}/${wizard.maxMana}")
        Text("HP Potions: ${wizard.healthPotions}")
        Text("MP Potions: ${wizard.manaPotions}")

        Spacer(modifier = Modifier.height(16.dp))

        enemy?.let {
            Text("${it.type}mon")
            Text("HP: ${it.currentHp}/${it.maxHp}")
            Text("Type: ${it.type}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(GameState.message)
        Spacer(modifier = Modifier.height(16.dp))

        // Battle actions
        Button(onClick = { GameState.castSpell(SpellType.FIRE) }) {
            Text("a. Fire Attack")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { GameState.castSpell(SpellType.WATER) }) {
            Text("b. Water Attack")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { GameState.castSpell(SpellType.GRASS) }) {
            Text("c. Grass Attack")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { GameState.drinkHealthPotion() }) {
            Text("d. Drink Health Potion")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { GameState.runFromBattle() }) {
            Text("e. Run")
        }
    }
}