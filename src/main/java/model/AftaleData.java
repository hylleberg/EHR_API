package model;

public class AftaleData {

    private String workerusername;
    private String cpr;
    private String datetime;
    private int duration;
    private String note;

    private int aftaleid;


    public int getAftaleid() {
        return aftaleid;
    }

    public void setAftaleid(int aftaleid) {
        this.aftaleid = aftaleid;
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
