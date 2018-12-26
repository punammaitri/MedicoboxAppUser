package com.aiprous.medicobox.prescription;

public class PrescriptionModel {

    String getDose;
    String prescription;

    public PrescriptionModel(String getDose, String prescription) {
        this.getDose = getDose;
        this.prescription = prescription;
    }

    public String getGetDose() {
        return getDose;
    }

    public void setGetDose(String getDose) {
        this.getDose = getDose;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    @Override
    public String toString() {
        return  getDose ;
    }
}
