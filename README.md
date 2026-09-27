# Smart Pantry Manager

An Android application built in **Java** for the Mobile App Development 700 module at Richfield Graduate Institute of Technology.

## Concept

Smart Pantry Manager helps users reduce food waste by tracking ingredients they have at home and suggesting recipes they can cook using **strictly** those leftover ingredients — no shopping trip required. A recipe is only suggested if the user genuinely has every required ingredient in sufficient quantity.

## Features

- **Pantry Management** — Add, edit, and delete pantry items with name, quantity, unit, and optional expiry date.
- **Recipe Collection** — 20 pre-loaded recipes stored in a local SQLite database.
- **Strict-Matching Suggestions** — The "Suggested Recipes" screen only shows recipes where every ingredient exists in the pantry in at least the required quantity.
- **Recipe Detail View** — Full ingredient list and step-by-step preparation instructions.
- **Settings Screen** — Toggle expiry alerts and unit preferences.
- **Persistent Storage** — All data survives app restarts using SQLite.

## Database Choice: SQLite

I chose **SQLite** because:
- It is covered directly in the module's persistent data chapter.
- It requires no external internet connection or account setup.
- It is lightweight, fast, and sufficient for a single-user local pantry app.
- Room/SQLiteOpenHelper is the standard Android approach for local relational data.

## Tech Stack

- **Language:** Java
- **IDE:** Android Studio
- **Min SDK:** 24
- **Target SDK:** 34
- **Database:** SQLite (SQLiteOpenHelper)
- **UI Components:** RecyclerView with custom Adapters, BottomNavigationView, ConstraintLayout, CardView
- **Navigation:** Bottom Navigation Bar with Fragment swapping

## Screens

1. **Pantry List** — View all pantry ingredients in a RecyclerView; tap + to add, or Edit/Delete existing items.
2. **Suggested Recipes** — Shows only recipes you can make right now based on strict pantry matching.
3. **Recipe Detail** — View full ingredients and preparation steps for a selected recipe.
4. **Add/Edit Ingredient** — Form with input validation for adding or updating pantry items.
5. **Settings** — Toggle preferences (expiry alerts, metric units).

## Strict-Matching Algorithm

The core business logic lives in `StrictMatcher.java`. It normalizes ingredient names (lowercase, trim, remove punctuation), handles basic singular/plural equivalence (e.g., "tomato" vs "tomatoes"), normalizes units (e.g., "gram" vs "g"), and only includes a recipe if **every** required ingredient is found in the pantry with sufficient quantity.

## How to Run

1. Clone this repository.
2. Open the project in **Android Studio**.
3. Sync Gradle files.
4. Run on an emulator or physical device (API 24+).
5. The app will seed 20 recipes on first launch. Add pantry items to see matching recipes.

## Git Commit History

This repository was developed incrementally with regular commits reflecting genuine development steps (project setup, database layer, UI screens, adapters, strict-matching logic, bug fixes, etc.).
