package com.example.androidinvestingcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale
import com.example.androidinvestingcalculator.ui.theme.AndroidInvestingCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidInvestingCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompoundInterestCalculator()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompoundInterestCalculator() {
    var principal by remember { mutableStateOf(TextFieldValue("")) }
    var monthlyContribution by remember { mutableStateOf(TextFieldValue("")) }
    var interestRate by remember { mutableStateOf(TextFieldValue("")) }
    var years by remember { mutableStateOf(TextFieldValue("")) }
    var compoundingPeriods by remember { mutableStateOf(TextFieldValue("12")) } // Default: Monthly
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Compound Interest Calculator", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = principal,
            onValueChange = { principal = it },
            label = { Text("Initial Deposit ($)") }
        )
        OutlinedTextField(
            value = monthlyContribution,
            onValueChange = { monthlyContribution = it },
            label = { Text("Monthly Contribution ($)") }
        )
        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text("Annual Interest Rate (%)") }
        )
        OutlinedTextField(
            value = years,
            onValueChange = { years = it },
            label = { Text("Number of Years") }
        )
        OutlinedTextField(
            value = compoundingPeriods,
            onValueChange = { compoundingPeriods = it },
            label = { Text("Compounding Periods per Year") }
        )
        Button(onClick = {
            result = calculateCompoundInterest(
                principal.text.toDoubleOrNull() ?: 0.0,
                monthlyContribution.text.toDoubleOrNull() ?: 0.0,
                (interestRate.text.toDoubleOrNull() ?: 0.0) / 100,
                years.text.toIntOrNull() ?: 0,
                compoundingPeriods.text.toIntOrNull() ?: 12
            )
        }) {
            Text("Calculate")
        }
        if (result.isNotEmpty()) {
            Text("Final Amount: $result", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun calculateCompoundInterest(principal: Double, monthlyContribution: Double, interestRate: Double, time: Int, compoundingPeriods: Int): String {
    val amount = principal * Math.pow(1 + (interestRate / compoundingPeriods), (compoundingPeriods * time).toDouble()) +
            (monthlyContribution * ((Math.pow(1 + (interestRate / compoundingPeriods), (compoundingPeriods * time).toDouble()) - 1) / (interestRate / compoundingPeriods)))
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}
