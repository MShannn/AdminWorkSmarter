package com.jvidal.worksmarter.RealmDatabase;

import io.realm.RealmObject;

public class WorkListDatabase extends RealmObject {

    public WorkListDatabase() {
    }


    String code;
    String ref;
    String address;
    String client_one;
    String client_two;
    String client_three;
    String fileName;
    String url;
    int row;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
