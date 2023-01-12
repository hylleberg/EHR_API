package model;

import java.sql.Timestamp;

public class AftaleData {

    private String workerusername;
    private String cpr;
    private String datetime;
    private int duration;
    private String note;

    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getWorkerusername() {
        return workerusername;
    }

    public void setWorkerusername(String workerusername) {
        this.workerusername = workerusername;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {

        String tempDatetime = datetime.replace(" ", "T");
        String returnDatetime = tempDatetime + "Z";
        this.datetime = returnDatetime;
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
