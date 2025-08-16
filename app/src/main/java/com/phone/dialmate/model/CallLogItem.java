package com.phone.dialmate.model;

public class CallLogItem {
    private String number;
    private int type;       // 1 Incoming, 2 Outgoing, 3 Missed
    private String date;    // display-formatted
    private String name;    // may be null

    public CallLogItem(String number, int type, String date, String name) {
        this.number = number;
        this.type = type;
        this.date = date;
        this.name = name;
    }

    public String getNumber() { return number; }
    public int getType() { return type; }
    public String getDate() { return date; }
    public String getName() { return name; }

    public String getTypeString() {
        switch (type) {
            case 1: return "Incoming";
            case 2: return "Outgoing";
            case 3: return "Missed";
            default: return "Unknown";
        }
    }
}
