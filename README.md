# Coinvert: Currency Exchange Application

Coinvert is an Android application that enables users to convert currencies based on live exchange rates. 

---

## Developers
- **Patricia Madrid**
- **Faith Ononye**

---

## Features

- Convert between 20+ currencies using real-time exchange rates
- Search countries to see what currency they use
- Favorite countries and access them in a separate tab
- Navigation drawer with quick access to Country Lookup
- Favorites saved using SharedPreferences
- Responsive UI with accessible touch targets

---

##  How It Works

- Users can input an amount and select a "from" and "to" currency.
- When they click "Convert", the app fetches real-time exchange rates using a REST API (ExchangeRate-API).
- The conversion is calculated and displayed.
- Users can search up a country and favorite/unfavorite.

---

## Tech Stack

- **Language:** Kotlin
- **IDE:** Android Studio
- **API:** [ExchangeRate-API](https://www.exchangerate-api.com)
- **UI Components:**
  - 'DrawerLayout' and 'NavigationView' for menu
  - 'ViewPager2' and 'TabLayout' for tab navigation
  - 'ListView' with custom item layout for countries
- **Storage:** SharedPrederences (for storing Favorites)

---

## Screens

- **Main Screen:** Currency converter with two dropdowns and result display
- **Country Lookup:** Searchable country list with star toggle
- **Favorites:** Tab showing only countries marked as favorite

---

## Installation

1. Clone this repo
2. Open in Android Studio
3. Add your Exchange-API key (if-applicable)
4. Run the app on an emulator or device

---

## License

This project is for educational/demo purposes. No license currently applied.

---

Made with love for CS 377 project work.
   

