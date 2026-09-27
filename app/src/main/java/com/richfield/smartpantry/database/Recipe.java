package com.richfield.smartpantry.database;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private List<Ingredient> ingredients;
    private String preparationSteps;

    public Recipe() {}

    public Recipe(int id, String name, String description, List<Ingredient> ingredients, String preparationSteps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.preparationSteps = preparationSteps;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public String getPreparationSteps() { return preparationSteps; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
}
