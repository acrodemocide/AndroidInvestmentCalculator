package com.algorithmicworldautomation.androidinvestingcalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algorithmicworldautomation.androidinvestingcalculator.ui.theme.AndroidInvestingCalculatorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidInvestingCalculatorTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope() // Used for launching coroutines
    var currentScreen by remember { mutableStateOf("CompoundInterest") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet  {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Navigation", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    NavigationDrawerItem(
                        label = { Text("Compound Interest Calculator") },
                        selected = currentScreen == "CompoundInterest",
                        onClick = {
                            currentScreen = "CompoundInterest"
                            coroutineScope.launch { drawerState.close() } // Open/close inside coroutine
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Real Estate Calculator") },
                        selected = currentScreen == "RealEstate",
                        onClick = {
                            currentScreen = "RealEstate"
                            coroutineScope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Investment Calculator") },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                when (currentScreen) {
                    "CompoundInterest" -> CompoundInterestCalculator()
                    "RealEstate" -> RealEstateCalculatorScreen()
                }
            }
        }
    }
}
