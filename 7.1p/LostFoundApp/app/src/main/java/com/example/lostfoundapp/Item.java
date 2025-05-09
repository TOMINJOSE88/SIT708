package com.example.lostfoundapp;

import java.io.Serializable;

public class Item implements Serializable {
    public int id;
    public String type, name, phone, description, date, location;

    public Item(int id, String type, String name, String phone, String description, String date, String location) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }
}
