package com.jvidal.worksmarter.RealmDatabase;

import io.realm.RealmObject;

public class InformationDatabase extends RealmObject {


    String code;
    String address;
    Double lattitude;
    Double longitude;
    int row;
    String orignalDatabaseCode;
    /*    String obervationProbe;
        String civilProblem;
        String electricProblem;*/
    String databaseName;
    String databaseURL;
    String databaseStructureType;

    public InformationDatabase() {
    }

    public String getDatabaseStructureType() {
        return databaseStructureType;
    }

    public void setDatabaseStructureType(String databaseStructureType) {
        this.databaseStructureType = databaseStructureType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public String getOrignalDatabaseCode() {
        return orignalDatabaseCode;
    }

    public void setOrignalDatabaseCode(String orignalDatabaseCode) {
        this.orignalDatabaseCode = orignalDatabaseCode;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
