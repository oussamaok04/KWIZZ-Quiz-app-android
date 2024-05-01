package com.example.quizapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Category {
    public Map categories = new HashMap();

    public Category(){
        categories.put("Sports", 21);
        categories.put("Geography", 22);
        categories.put("History", 23);
        categories.put("Art", 25);
        categories.put("Animals", 26);
        categories.put("Vehicles", 27);
        categories.put("Anime & Manga", 31);
        categories.put("Mathematics", 19);
        categories.put("Music", 12);
        categories.put("Movies", 12);
    }

    public Set<String> getCategoryList(){
        return categories.keySet();
    }

    public Map getCategories() {
        return categories;
    }
}
