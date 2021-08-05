package com.jvidal.worksmarter.Models;

public class WorkListModel {


    String problem;
    int row;
    Double lati;
    Double longi;
    String code;
    String ref;
    String address;
    String client_one;
    String client_two;
    String client_three;
    String fileUrl;
    String fileName;
    String finalCode;
    String typeOfStructure;

    public WorkListModel() {
    }

    public String getTypeOfStructure() {
        return typeOfStructure;
    }

    public void setTypeOfStructure(String typeOfStructure) {
        this.typeOfStructure = typeOfStructure;
    }

    public String getFinalCode() {
        return finalCode;
    }

    public void setFinalCode(String finalCode) {
        this.finalCode = finalCode;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient_one() {
        return client_one;
    }

    public void setClient_one(String client_one) {
        this.client_one = client_one;
    }

    public String getClient_two() {
        return client_two;
    }

    public void setClient_two(String client_two) {
        this.client_two = client_two;
    }

    public String getClient_three() {
        return client_three;
    }

    public void setClient_three(String client_three) {
        this.client_three = client_three;
    }
}
