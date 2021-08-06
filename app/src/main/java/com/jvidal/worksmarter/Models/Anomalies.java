package com.jvidal.worksmarter.Models;

import java.util.Date;

public class Anomalies {

    public String code;
    public String obervation;
    public String civilAnomaly;
    public String electricAnomaly;
    public String objectId;
    public Date created;
    public String billboard;

    public String getBillboard() {
        return billboard;
    }

    public void setBillboard(String billboard) {
        this.billboard = billboard;
    }

    public Date updated;
    int isActive;

    public Anomalies() {
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getObervation() {
        return obervation;
    }

    public void setObervation(String obervation) {
        this.obervation = obervation;
    }

    public String getCivilAnomaly() {
        return civilAnomaly;
    }

    public void setCivilAnomaly(String civilAnomaly) {
        this.civilAnomaly = civilAnomaly;
    }

    public String getElectricAnomaly() {
        return electricAnomaly;
    }

    public void setElectricAnomaly(String electricAnomaly) {
        this.electricAnomaly = electricAnomaly;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
