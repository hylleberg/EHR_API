package model;

import java.sql.Timestamp;

public class RequestData {

    private String datetime;

    private String timeOfDay;

    private String comment;

    private String workerusername;

    private String cpr;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
