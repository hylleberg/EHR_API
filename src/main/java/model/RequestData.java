package model;

public class RequestData {

    private String cpr;
    private String datetime;

    private String timeOfDay;

    private String note;

    private String workerusername;

    private int aftaleid;

    public int getAftaleid() {
        return aftaleid;
    }

    public void setAftaleid(int aftaleid) {
        this.aftaleid = aftaleid;
    }

    public String getDatetime() {
        return datetime.toString();
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWorkerusername() {
        return workerusername;
    }

    public void setWorkerusername(String workerusername) {
        this.workerusername = workerusername;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }
}
