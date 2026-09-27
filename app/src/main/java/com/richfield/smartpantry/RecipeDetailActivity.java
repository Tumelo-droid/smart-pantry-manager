package com.richfield.smartpantry;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.richfield.smartpantry.database.DatabaseHelper;
import com.richfield.smartpantry.database.Ingredient;
import com.richfield.smartpantry.database.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvName, tvDesc, tvIngredients, tvSteps;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvName = findViewById(R.id.tvDetailName);
        tvDesc = findViewById(R.id.tvDetailDesc);
        tvIngredients = findViewById(R.id.tvDetailIngredients);
        tvSteps = findViewById(R.id.tvDetailSteps);

        dbHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    private void loadRecipe(int id) {
        Recipe recipe = dbHelper.getRecipe(id);
        if (recipe != null) {
            tvName.setText(recipe.getName());
            tvDesc.setText(recipe.getDescription());

            StringBuilder ingBuilder = new StringBuilder();
            for (Ingredient ing : recipe.getIngredients()) {
                ingBuilder.append("• ")
                        .append(capitalize(ing.getName()))
                        .append(" - ")
                        .append(ing.getQuantity())
                        .append(" ")
                        .append(ing.getUnit())
                        .append("\n");
            }
            tvIngredients.setText(ingBuilder.toString().trim());
            tvSteps.setText(recipe.getPreparationSteps());
            setTitle(recipe.getName());
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
