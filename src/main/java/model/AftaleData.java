package model;

import java.sql.Timestamp;

public class AftaleData {

    private Timestamp datetime;
    private int duration;
    private String note;
    private String cprkey;

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCprkey() {
        return cprkey;
    }

    public void setCprkey(String cprkey) {
        this.cprkey = cprkey;
    }





}
