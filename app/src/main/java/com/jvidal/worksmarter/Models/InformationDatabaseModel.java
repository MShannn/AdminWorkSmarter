package com.jvidal.worksmarter.Models;

public class InformationDatabaseModel {

    String code;
    String address;
    Double lattitude;
    Double longitude;
    int row;
    String orignalDatabasecode;
    /*    String obervationProbe;
        String civilProblem;
        String electricProblem;*/

    String databaseName;
    String databaseURL;

    String databaseStructureType;

    public String getDatabaseStructureType() {
        return databaseStructureType;
    }

    public void setDatabaseStructureType(String databaseStructureType) {
        this.databaseStructureType = databaseStructureType;
    }

    public InformationDatabaseModel() {

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

    public String getOrignalDatabasecode() {
        return orignalDatabasecode;
    }

    public void setOrignalDatabasecode(String orignalDatabasecode) {
        this.orignalDatabasecode = orignalDatabasecode;
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


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
