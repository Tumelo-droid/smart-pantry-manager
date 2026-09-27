package com.richfield.smartpantry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    private static final String TABLE_PANTRY = "pantry";
    private static final String COL_PANTRY_ID = "id";
    private static final String COL_PANTRY_NAME = "name";
    private static final String COL_PANTRY_QUANTITY = "quantity";
    private static final String COL_PANTRY_UNIT = "unit";
    private static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipes table
    private static final String TABLE_RECIPES = "recipes";
    private static final String COL_RECIPE_ID = "id";
    private static final String COL_RECIPE_NAME = "name";
    private static final String COL_RECIPE_DESC = "description";
    private static final String COL_RECIPE_STEPS = "steps";

    // Recipe ingredients table
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String COL_RI_ID = "id";
    private static final String COL_RI_RECIPE_ID = "recipe_id";
    private static final String COL_RI_NAME = "name";
    private static final String COL_RI_QUANTITY = "quantity";
    private static final String COL_RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " ("
                + COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PANTRY_NAME + " TEXT NOT NULL, "
                + COL_PANTRY_QUANTITY + " REAL NOT NULL, "
                + COL_PANTRY_UNIT + " TEXT NOT NULL, "
                + COL_PANTRY_EXPIRY + " TEXT)";

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " ("
                + COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_NAME + " TEXT NOT NULL, "
                + COL_RECIPE_DESC + " TEXT, "
                + COL_RECIPE_STEPS + " TEXT NOT NULL)";

        String createRecipeIngredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " ("
                + COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RI_RECIPE_ID + " INTEGER NOT NULL, "
                + COL_RI_NAME + " TEXT NOT NULL, "
                + COL_RI_QUANTITY + " REAL NOT NULL, "
                + COL_RI_UNIT + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))";

        db.execSQL(createPantry);
        db.execSQL(createRecipes);
        db.execSQL(createRecipeIngredients);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // ===================== PANTRY CRUD =====================

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, normalizeName(item.getName()));
        values.put(COL_PANTRY_QUANTITY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit().toLowerCase());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate() != null ? item.getExpiryDate() : "");
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToPantryItem(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public PantryItem getPantryItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        PantryItem item = null;
        if (cursor.moveToFirst()) {
            item = cursorToPantryItem(cursor);
        }
        cursor.close();
        db.close();
        return item;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, normalizeName(item.getName()));
        values.put(COL_PANTRY_QUANTITY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit().toLowerCase());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate() != null ? item.getExpiryDate() : "");
        int rows = db.update(TABLE_PANTRY, values, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ===================== RECIPES =====================

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToRecipe(cursor, db));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = cursorToRecipe(cursor, db);
        }
        cursor.close();
        db.close();
        return recipe;
    }

    private List<Ingredient> getIngredientsForRecipe(SQLiteDatabase db, int recipeId) {
        List<Ingredient> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RI_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Ingredient(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RI_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_RI_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RI_UNIT))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // ===================== HELPERS =====================

    private PantryItem cursorToPantryItem(Cursor cursor) {
        return new PantryItem(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_PANTRY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PANTRY_QUANTITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY))
        );
    }

    private Recipe cursorToRecipe(Cursor cursor, SQLiteDatabase db) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID));
        return new Recipe(
                id,
                cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_DESC)),
                getIngredientsForRecipe(db, id),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS))
        );
    }

    public static String normalizeName(String name) {
        if (name == null) return "";
        return name.toLowerCase().trim();
    }

    // ===================== SEED DATA =====================

    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Tomato Basil Pasta",
                "A simple and delicious pasta dish",
                "1. Boil pasta according to package instructions.\n2. Sauté garlic in olive oil.\n3. Add chopped tomatoes and basil.\n4. Toss with cooked pasta and serve.",
                new Ingredient[]{new Ingredient("pasta", 250, "g"), new Ingredient("tomato", 3, "pcs"), new Ingredient("garlic", 2, "cloves"), new Ingredient("basil", 10, "leaves"), new Ingredient("olive oil", 2, "tbsp")});

        addRecipe(db, "Scrambled Eggs with Toast",
                "Quick breakfast classic",
                "1. Whisk eggs with salt and pepper.\n2. Cook in a buttered pan over low heat.\n3. Serve with buttered toast.",
                new Ingredient[]{new Ingredient("egg", 3, "pcs"), new Ingredient("bread", 2, "slices"), new Ingredient("butter", 20, "g"), new Ingredient("milk", 30, "ml")});

        addRecipe(db, "Vegetable Stir Fry",
                "Healthy and colourful stir fry",
                "1. Chop all vegetables.\n2. Heat oil in a wok.\n3. Stir fry vegetables for 5-7 minutes.\n4. Add soy sauce and serve over rice.",
                new Ingredient[]{new Ingredient("carrot", 2, "pcs"), new Ingredient("bell pepper", 1, "pcs"), new Ingredient("broccoli", 150, "g"), new Ingredient("soy sauce", 2, "tbsp"), new Ingredient("rice", 200, "g"), new Ingredient("onion", 1, "pcs")});

        addRecipe(db, "Cheese Omelette",
                "Simple fluffy omelette",
                "1. Beat eggs with a splash of milk.\n2. Pour into a hot buttered pan.\n3. Add grated cheese.\n4. Fold and cook until set.",
                new Ingredient[]{new Ingredient("egg", 2, "pcs"), new Ingredient("cheese", 50, "g"), new Ingredient("butter", 10, "g"), new Ingredient("milk", 20, "ml")});

        addRecipe(db, "Garlic Butter Rice",
                "Fragrant side dish",
                "1. Cook rice in salted water.\n2. Melt butter and sauté garlic.\n3. Mix with cooked rice.",
                new Ingredient[]{new Ingredient("rice", 200, "g"), new Ingredient("garlic", 3, "cloves"), new Ingredient("butter", 20, "g")});

        addRecipe(db, "Tomato Soup",
                "Warm and comforting",
                "1. Sauté onion and garlic.\n2. Add chopped tomatoes and stock.\n3. Simmer for 20 minutes.\n4. Blend until smooth.",
                new Ingredient[]{new Ingredient("tomato", 6, "pcs"), new Ingredient("onion", 1, "pcs"), new Ingredient("garlic", 2, "cloves"), new Ingredient("vegetable stock", 500, "ml")});

        addRecipe(db, "Pancakes",
                "Fluffy breakfast pancakes",
                "1. Mix flour, milk, and egg into a batter.\n2. Cook spoonfuls on a hot pan.\n3. Flip when bubbles appear.",
                new Ingredient[]{new Ingredient("flour", 150, "g"), new Ingredient("milk", 200, "ml"), new Ingredient("egg", 1, "pcs"), new Ingredient("butter", 20, "g")});

        addRecipe(db, "Fried Rice",
                "Quick one-pan meal",
                "1. Cook rice and let cool.\n2. Stir fry vegetables and egg.\n3. Add rice and soy sauce.\n4. Toss until heated through.",
                new Ingredient[]{new Ingredient("rice", 250, "g"), new Ingredient("egg", 2, "pcs"), new Ingredient("carrot", 1, "pcs"), new Ingredient("soy sauce", 2, "tbsp"), new Ingredient("onion", 1, "pcs")});

        addRecipe(db, "Pasta Aglio e Olio",
                "Classic Italian simplicity",
                "1. Cook pasta.\n2. Sauté garlic in olive oil until golden.\n3. Toss pasta with garlic oil and chilli flakes.",
                new Ingredient[]{new Ingredient("pasta", 250, "g"), new Ingredient("garlic", 4, "cloves"), new Ingredient("olive oil", 3, "tbsp"), new Ingredient("chilli flakes", 1, "tsp")});

        addRecipe(db, "Cheese Toastie",
                "Crispy melted cheese sandwich",
                "1. Butter bread slices.\n2. Add cheese between slices.\n3. Grill until golden and melted.",
                new Ingredient[]{new Ingredient("bread", 2, "slices"), new Ingredient("cheese", 60, "g"), new Ingredient("butter", 15, "g")});

        addRecipe(db, "Boiled Egg Salad",
                "Light and protein-rich",
                "1. Boil eggs for 8 minutes.\n2. Chop vegetables.\n3. Mix with olive oil dressing.",
                new Ingredient[]{new Ingredient("egg", 2, "pcs"), new Ingredient("tomato", 2, "pcs"), new Ingredient("onion", 0.5, "pcs"), new Ingredient("olive oil", 1, "tbsp")});

        addRecipe(db, "Rice and Beans",
                "Hearty vegetarian dish",
                "1. Cook rice.\n2. Sauté onion and garlic.\n3. Add beans and simmer.\n4. Serve over rice.",
                new Ingredient[]{new Ingredient("rice", 200, "g"), new Ingredient("beans", 150, "g"), new Ingredient("onion", 1, "pcs"), new Ingredient("garlic", 2, "cloves"), new Ingredient("tomato", 2, "pcs")});

        addRecipe(db, "Simple Pasta Salad",
                "Cold pasta dish for lunch",
                "1. Cook and cool pasta.\n2. Chop vegetables.\n3. Mix with olive oil and serve.",
                new Ingredient[]{new Ingredient("pasta", 200, "g"), new Ingredient("tomato", 2, "pcs"), new Ingredient("onion", 0.5, "pcs"), new Ingredient("olive oil", 2, "tbsp")});

        addRecipe(db, "Garlic Bread",
                "Crispy aromatic side",
                "1. Mix butter with crushed garlic.\n2. Spread on bread slices.\n3. Bake until crispy.",
                new Ingredient[]{new Ingredient("bread", 4, "slices"), new Ingredient("butter", 30, "g"), new Ingredient("garlic", 3, "cloves")});

        addRecipe(db, "Egg Fried Noodles",
                "Quick Asian-style noodles",
                "1. Boil noodles.\n2. Scramble eggs in a pan.\n3. Add noodles and soy sauce.\n4. Toss and serve.",
                new Ingredient[]{new Ingredient("noodles", 200, "g"), new Ingredient("egg", 2, "pcs"), new Ingredient("soy sauce", 2, "tbsp"), new Ingredient("onion", 1, "pcs")});

        addRecipe(db, "Buttered Rice with Egg",
                "Comfort food simplicity",
                "1. Cook rice.\n2. Fry egg sunny-side up.\n3. Serve egg on rice with butter.",
                new Ingredient[]{new Ingredient("rice", 200, "g"), new Ingredient("egg", 1, "pcs"), new Ingredient("butter", 15, "g")});

        addRecipe(db, "Tomato Omelette",
                "Fresh and light omelette",
                "1. Beat eggs.\n2. Pour into pan.\n3. Add diced tomatoes and fold.",
                new Ingredient[]{new Ingredient("egg", 2, "pcs"), new Ingredient("tomato", 1, "pcs"), new Ingredient("butter", 10, "g")});

        addRecipe(db, "Vegetable Soup",
                "Warm nourishing soup",
                "1. Chop all vegetables.\n2. Simmer in stock for 30 minutes.\n3. Season and serve.",
                new Ingredient[]{new Ingredient("carrot", 2, "pcs"), new Ingredient("onion", 1, "pcs"), new Ingredient("potato", 2, "pcs"), new Ingredient("vegetable stock", 600, "ml")});

        addRecipe(db, "Cheesy Rice Bowl",
                "Creamy rice dish",
                "1. Cook rice.\n2. Stir in grated cheese until melted.\n3. Season and serve.",
                new Ingredient[]{new Ingredient("rice", 200, "g"), new Ingredient("cheese", 80, "g"), new Ingredient("butter", 10, "g"), new Ingredient("milk", 50, "ml")});

        addRecipe(db, "Classic French Toast",
                "Sweet breakfast treat",
                "1. Whisk egg with milk and cinnamon.\n2. Dip bread slices.\n3. Fry until golden on both sides.",
                new Ingredient[]{new Ingredient("bread", 2, "slices"), new Ingredient("egg", 1, "pcs"), new Ingredient("milk", 50, "ml"), new Ingredient("butter", 15, "g")});
    }

    private void addRecipe(SQLiteDatabase db, String name, String desc, String steps, Ingredient[] ingredients) {
        ContentValues rv = new ContentValues();
        rv.put(COL_RECIPE_NAME, name);
        rv.put(COL_RECIPE_DESC, desc);
        rv.put(COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, rv);

        for (Ingredient ing : ingredients) {
            ContentValues iv = new ContentValues();
            iv.put(COL_RI_RECIPE_ID, recipeId);
            iv.put(COL_RI_NAME, normalizeName(ing.getName()));
            iv.put(COL_RI_QUANTITY, ing.getQuantity());
            iv.put(COL_RI_UNIT, ing.getUnit().toLowerCase());
            db.insert(TABLE_RECIPE_INGREDIENTS, null, iv);
        }
    }
}
