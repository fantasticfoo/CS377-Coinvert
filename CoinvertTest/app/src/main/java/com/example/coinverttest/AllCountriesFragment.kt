package com.example.coinverttest

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

class AllCountriesFragment : Fragment() {

    private val countryCurrencyMap = CountryData.countryCurrencyMap

    private lateinit var listView: ListView
    private lateinit var searchBox: EditText
    private var currentList = countryCurrencyMap.keys.sorted()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_country_list, container, false)

        listView = view.findViewById(R.id.countryListView)
        searchBox = view.findViewById(R.id.searchBox)

        updateList(currentList)

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                currentList = countryCurrencyMap.keys.filter { it.contains(query, ignoreCase = true) }.sorted()
                updateList(currentList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        updateList(CountryData.countryCurrencyMap.keys.sorted())
    }

    private fun updateList(list: List<String>) {
        listView.adapter = object : ArrayAdapter<String>(requireContext(), R.layout.list_item_country, R.id.textCountryName, list) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val row = super.getView(position, convertView, parent)
                val country = list[position]
                val favoriteBtn = row.findViewById<ImageButton>(R.id.buttonFavorite)
                val prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
                val isFavorited = prefs.getBoolean(country, false)
                favoriteBtn.setImageResource(
                    if (isFavorited) android.R.drawable.btn_star_big_on
                    else android.R.drawable.btn_star_big_off
                )
                favoriteBtn.setOnClickListener {
                    val newStatus = !isFavorited
                    prefs.edit().putBoolean(country, newStatus).apply()
                    notifyDataSetChanged()
                    Toast.makeText(requireContext(),
                        if (newStatus) "$country added to Favorites" else "$country removed from Favorites",
                        Toast.LENGTH_SHORT).show()
                }
                row.setOnClickListener {
                    val currency = countryCurrencyMap[country]
                    Toast.makeText(requireContext(), "$country uses $currency", Toast.LENGTH_SHORT).show()
                }
                return row
            }
        }
    }
}


