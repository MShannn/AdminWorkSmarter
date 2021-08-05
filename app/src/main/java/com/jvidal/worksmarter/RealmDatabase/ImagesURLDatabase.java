package com.jvidal.worksmarter.RealmDatabase;

import io.realm.RealmObject;

public class ImagesURLDatabase extends RealmObject {


    String code;
    String client;
    String photoPath;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public ImagesURLDatabase() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
