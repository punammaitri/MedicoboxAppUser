package com.aiprous.medicobox.prescription;

import java.util.ArrayList;

public class GetImageUrlModel {

    String imageUrl;
    String patientName;
    String additionalComment;
    String uploadPrescriptionFlag;
    String getDoseParam;

    public GetImageUrlModel(String imageUrl, String patientName, String additionalComment, String uploadPrescriptionFlag, String getDoseParam) {
        this.imageUrl = imageUrl;
        this.patientName = patientName;
        this.additionalComment = additionalComment;
        this.uploadPrescriptionFlag = uploadPrescriptionFlag;
        this.getDoseParam = getDoseParam;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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