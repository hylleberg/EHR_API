package model;

import java.util.List;

public class EKGData {



    private String cpr;
    private String startTime;


    private List<Float> ekgDataList;

    private int ekgID;

    public int getEkgID() {
        return ekgID;
    }

    public void setEkgID(int ekgID) {
        this.ekgID = ekgID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public List<Float> getEkgDataList() {
        return ekgDataList;
    }

    public void setEkgDataList(List<Float> ekgDataList) {
        this.ekgDataList = ekgDataList;
    }



}
