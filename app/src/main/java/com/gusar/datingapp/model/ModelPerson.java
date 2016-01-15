package com.gusar.datingapp.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by evgeniy on 14.01.16.
 */
public class ModelPerson {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String location;

    @DatabaseField
    private String status;

    @DatabaseField
    private String photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
