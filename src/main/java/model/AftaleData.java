package model;

import java.sql.Timestamp;

public class AftaleData {

    private Timestamp datetime;
    private int duration;
    private String note;

    public String getDatetime() {
        return datetime.toString();
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






}
