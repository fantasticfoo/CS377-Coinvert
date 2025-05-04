package com.example.coinverttest

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

class FavoritesFragment : Fragment() {

    private lateinit var favorites: List<String>
    private lateinit var listView: ListView
    private lateinit var searchBox: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_country_list, container, false)

        listView = view.findViewById(R.id.countryListView)
        searchBox = view.findViewById(R.id.searchBox)

        val prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        favorites = prefs.all.filterValues { it == true }.keys.toList().sorted()
        updateList(favorites)

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = favorites.filter { it.contains(s.toString(), ignoreCase = true) }
                updateList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        favorites = prefs.all.filterValues { it == true }.keys.toList().sorted()
        updateList(favorites)
    }

    private fun updateList(list: List<String>) {
        listView.adapter = object : ArrayAdapter<String>(requireContext(), R.layout.list_item_country, R.id.textCountryName, list) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val row = super.getView(position, convertView, parent)
                val country = list[position]
                val favoriteBtn = row.findViewById<ImageButton>(R.id.buttonFavorite)
                val prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)

                favoriteBtn.setImageResource(android.R.drawable.btn_star_big_on)

                favoriteBtn.setOnClickListener {
                    prefs.edit().remove(country).apply()
                    favorites = prefs.all.filterValues { it == true }.keys.toList().sorted()
                    updateList(favorites)
                    Toast.makeText(requireContext(), "$country removed from Favorites", Toast.LENGTH_SHORT).show()
                }

                row.setOnClickListener {
                    val currency = CountryData.countryCurrencyMap[country] ?: "Unknown"
                    Toast.makeText(requireContext(), "$country uses $currency", Toast.LENGTH_SHORT).show()
                }

                return row
            }
        }
    }
}



