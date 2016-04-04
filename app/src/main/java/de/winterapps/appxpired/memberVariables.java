package de.winterapps.appxpired;

import android.app.Application;

public class memberVariables extends Application{

    public String name;
    public String brand;
    public String size;
    public localDatabase database;

    public localDatabase getDatabase() {
        return database;
    }

    public void initDatabase() {
        this.database = new localDatabase(this);
    }

    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String EAN;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
