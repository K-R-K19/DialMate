package com.phone.dialmate.model;

public class Contact {
    private String id;
    private String name;
    private String phoneNumber;

    // 3-arg constructor
    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // 2-arg convenience constructor (id not known)
    public Contact(String name, String phoneNumber) {
        this(null, name, phoneNumber);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
