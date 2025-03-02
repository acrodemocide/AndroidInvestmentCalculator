package com.algorithmicworldautomation.androidinvestingcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class RealEstateCalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealEstateCalculatorScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateCalculatorScreen() {
    var salePrice by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var mortgageType by remember { mutableStateOf("No Mortgage") }
    var interestRate by remember { mutableStateOf("") }
    var downPayment by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val mortgageOptions = listOf("No Mortgage", "30-Year Mortgage", "15-Year Mortgage")

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = salePrice,
            onValueChange = { salePrice = it },
            label = { Text("Sale Price") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = rent,
            onValueChange = { rent = it },
            label = { Text("Estimated Rent") },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuExample(mortgageOptions, mortgageType) { selected ->
            mortgageType = selected
        }
        if (mortgageType != "No Mortgage") {
            OutlinedTextField(
                value = interestRate,
                onValueChange = { interestRate = it },
                label = { Text("Interest Rate (%)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = downPayment,
                onValueChange = { downPayment = it },
                label = { Text("Down Payment") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(
            onClick = {
                result = calculateInvestmentMetrics(
                    salePrice.toDoubleOrNull() ?: 0.0,
                    rent.toDoubleOrNull() ?: 0.0,
                    mortgageType,
                    interestRate.toDoubleOrNull() ?: 0.0,
                    downPayment.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }
        Text(result, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun DropdownMenuExample(options: List<String>, selectedOption: String, onSelectionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }) {
            Text(selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectionChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun calculateInvestmentMetrics(salePrice: Double, rent: Double, mortgageType: String, interestRate: Double, downPayment: Double): String {
    val loanAmount = if (mortgageType == "No Mortgage") 0.0 else salePrice - downPayment
    val annualRent = rent * 12
    val operatingExpenses = annualRent * 0.5 // Assume 50% of rent goes to expenses
    val netOperatingIncome = annualRent - operatingExpenses
    val capRate = (netOperatingIncome / salePrice) * 100

    val cashInvested = if (mortgageType == "No Mortgage") salePrice else downPayment
    val cashFlow = netOperatingIncome - (loanAmount * interestRate / 100)
    val cashOnCashReturn = (cashFlow / cashInvested) * 100

    val sp500Return = cashInvested * 0.10 // Assuming 10% annual return in S&P 500

    return "Cap Rate: %.2f%%\nCash-on-Cash Return: %.2f%%\nS&P 500 Comparison: $%.2f per year".format(capRate, cashOnCashReturn, sp500Return)
}
