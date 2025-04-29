package com.example.sos;

public class ContactModel {
    String id;
    String name;
    String number;

    public ContactModel(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
