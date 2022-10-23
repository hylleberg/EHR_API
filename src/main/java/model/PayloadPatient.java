package model;

public class PayloadPatient<PatientData, Response>{
    public final PatientData pd;
    public final Response r;

    public PayloadPatient(PatientData pd, Response r){

        this.pd = pd;
        this.r = r;
    }
}
