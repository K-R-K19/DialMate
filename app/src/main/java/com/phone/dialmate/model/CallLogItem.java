package com.phone.dialmate.model;

import android.provider.CallLog;

public class CallLogItem {
    private final String number;
    private final int type;
    private final String date;
    private final String duration;

    public CallLogItem(String number, int type, String date, String duration) {
        this.number = number; this.type = type; this.date = date; this.duration = duration;
    }

    public String getNumber() { return number; }
    public String getDate() { return date; }
    public String getDuration() { return duration; }

    public String getCallTypeString() {
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE: return "Incoming";
            case CallLog.Calls.OUTGOING_TYPE: return "Outgoing";
            case CallLog.Calls.MISSED_TYPE:   return "Missed";
            case CallLog.Calls.REJECTED_TYPE: return "Rejected";
            case CallLog.Calls.VOICEMAIL_TYPE:return "Voicemail";
            default: return "Other";
        }
    }
}
