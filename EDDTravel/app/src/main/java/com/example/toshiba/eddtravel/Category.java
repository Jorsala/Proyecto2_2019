package com.example.toshiba.eddtravel;

import android.graphics.drawable.Drawable;

public class Category {
    private String title;
    private String categoryId;
    private String description;
    private String costo;
    private String tiempo;

    public Category() {
        super();
    }

    public Category(String categoryId, String title, String description, String costo, String tiempo) {
        super();
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.costo = costo;
        this.tiempo = tiempo;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getCategoryId(){return categoryId;}

    public void setCategoryId(String categoryId){this.categoryId = categoryId;}


}
