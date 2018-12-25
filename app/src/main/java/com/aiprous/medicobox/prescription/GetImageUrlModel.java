package com.aiprous.medicobox.prescription;

import java.util.ArrayList;

public class GetImageUrlModel {

    String patientName;
    String additionalComment;
    String uploadPrescriptionFlag;
    String getDoseParam;

    public GetImageUrlModel(String patientName, String additionalComment, String uploadPrescriptionFlag, String getDoseParam) {
        this.patientName = patientName;
        this.additionalComment = additionalComment;
        this.uploadPrescriptionFlag = uploadPrescriptionFlag;
        this.getDoseParam = getDoseParam;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public void setAdditionalComment(String additionalComment) {
        this.additionalComment = additionalComment;
    }

    public String getUploadPrescriptionFlag() {
        return uploadPrescriptionFlag;
    }

    public void setUploadPrescriptionFlag(String uploadPrescriptionFlag) {
        this.uploadPrescriptionFlag = uploadPrescriptionFlag;
    }

    public String getGetDoseParam() {
        return getDoseParam;
    }

    public void setGetDoseParam(String getDoseParam) {
        this.getDoseParam = getDoseParam;
    }
}