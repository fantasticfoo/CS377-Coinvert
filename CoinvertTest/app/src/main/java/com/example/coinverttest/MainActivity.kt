package com.example.coinverttest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var editAmount: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var buttonConvert: Button
    private lateinit var textResult: TextView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    private val currencies = listOf(
        "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "INR", "CHF", "CNY", "SEK",
        "NZD", "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "RUB", "ZAR", "BRL",
        "PLN", "DKK"
    ).sorted()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up toolbar as action bar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Drawer setup
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navigationView)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_country_lookup -> {
                    startActivity(Intent(this, CountryLookupActivity::class.java))
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

        // UI component binding
        editAmount = findViewById(R.id.editAmount)
        spinnerFrom = findViewById(R.id.spinnerFromCurrency)
        spinnerTo = findViewById(R.id.spinnerToCurrency)
        buttonConvert = findViewById(R.id.buttonConvert)
        textResult = findViewById(R.id.textResult)

        // Set up currency dropdowns
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Handle convert button click
        buttonConvert.setOnClickListener {
            val amountText = editAmount.text.toString()
            val fromCurrency = spinnerFrom.selectedItem.toString()
            val toCurrency = spinnerTo.selectedItem.toString()

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val service = RetrofitClient.instance
            service.getRates(fromCurrency).enqueue(object : Callback<ExchangeRateResponse> {
                override fun onResponse(
                    call: Call<ExchangeRateResponse>,
                    response: Response<ExchangeRateResponse>
                ) {
                    if (response.isSuccessful) {
                        val rate = response.body()?.conversion_rates?.get(toCurrency)
                        if (rate != null) {
                            val result = amount * rate
                            textResult.text = "$amount $fromCurrency = %.2f $toCurrency".format(result)
                        } else {
                            textResult.text = "Conversion rate not found"
                        }
                    } else {
                        textResult.text = "Failed to fetch exchange rates"
                        Log.e("MainActivity", "Error body: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                    textResult.text = "Error: ${t.message}"
                    Log.e("MainActivity", "Network error", t)
                }
            })
        }
    }

    // Support navigation toggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }
}




