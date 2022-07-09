package com.example.quizme.db;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class CategoryModel {
    private String Name;
    private int CategoryId;
    public CategoryModel(int CategoryId, String СategoryName) {
        this.CategoryId = CategoryId;
        this.Name = СategoryName;
       /* this.categoryImage = categoryImage;*/
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryModel that = (CategoryModel) o;
        return CategoryId == that.CategoryId && Objects.equals(Name, that.Name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(Name, CategoryId);
    }

    public CategoryModel() {}

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.CategoryId = categoryId;
    }

    public String getСategoryName() {
        return Name;
    }

    public void setСategoryName(String сategoryName) {
        this.Name = сategoryName;
    }

   /* public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }*/
}
