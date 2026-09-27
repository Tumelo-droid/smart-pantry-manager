package com.richfield.smartpantry.utils;

import com.richfield.smartpantry.database.Ingredient;
import com.richfield.smartpantry.database.PantryItem;
import com.richfield.smartpantry.database.Recipe;

import java.util.ArrayList;
import java.util.List;

public class StrictMatcher {

    /**
     * Returns recipes where EVERY required ingredient exists in the pantry
     * with at least the required quantity. Normalizes names and handles
     * basic singular/plural and unit equivalence.
     */
    public static List<Recipe> findMatchingRecipes(List<Recipe> allRecipes, List<PantryItem> pantry) {
        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantry)) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    private static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantry) {
        for (Ingredient required : recipe.getIngredients()) {
            if (!hasIngredientInPantry(required, pantry)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasIngredientInPantry(Ingredient required, List<PantryItem> pantry) {
        String reqName = normalize(required.getName());
        String reqUnit = normalizeUnit(required.getUnit());
        double reqQty = required.getQuantity();

        for (PantryItem item : pantry) {
            String panName = normalize(item.getName());
            String panUnit = normalizeUnit(item.getUnit());
            double panQty = item.getQuantity();

            if (namesMatch(reqName, panName) && unitsCompatible(reqUnit, panUnit)) {
                if (panQty >= reqQty) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean namesMatch(String a, String b) {
        return a.equals(b) || singularize(a).equals(singularize(b));
    }

    private static String normalize(String s) {
        return s.toLowerCase().trim().replaceAll("[^a-z0-9]", "");
    }

    private static String normalizeUnit(String unit) {
        String u = normalize(unit);
        // basic unit aliases
        if (u.equals("g") || u.equals("gram") || u.equals("grams")) return "g";
        if (u.equals("kg") || u.equals("kilogram") || u.equals("kilograms")) return "kg";
        if (u.equals("ml") || u.equals("milliliter") || u.equals("millilitre")) return "ml";
        if (u.equals("l") || u.equals("liter") || u.equals("litre") || u.equals("litres")) return "l";
        if (u.equals("tbsp") || u.equals("tablespoon") || u.equals("tablespoons")) return "tbsp";
        if (u.equals("tsp") || u.equals("teaspoon") || u.equals("teaspoons")) return "tsp";
        if (u.equals("pcs") || u.equals("piece") || u.equals("pieces") || u.equals("pc")) return "pcs";
        if (u.equals("slice") || u.equals("slices")) return "slice";
        if (u.equals("clove") || u.equals("cloves")) return "clove";
        if (u.equals("leaf") || u.equals("leaves")) return "leaf";
        return u;
    }

    private static boolean unitsCompatible(String a, String b) {
        return a.equals(b);
    }

    private static String singularize(String word) {
        if (word.endsWith("ies") && word.length() > 3) {
            return word.substring(0, word.length() - 3) + "y";
        }
        if (word.endsWith("es") && word.length() > 2) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("s") && word.length() > 1) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }
}
