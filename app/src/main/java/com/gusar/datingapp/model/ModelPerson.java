package com.gusar.datingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by evgeniy on 14.01.16.
 */
public class ModelPerson implements Parcelable{
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String location;

    @DatabaseField
    private String status;

    @DatabaseField
    private String photo;

    private boolean like = false;

    protected ModelPerson(Parcel in) {
        id = in.readInt();
        location = in.readString();
        status = in.readString();
        photo = in.readString();
        like = in.readByte() != 0;
    }

    public static final Creator<ModelPerson> CREATOR = new Creator<ModelPerson>() {
        @Override
        public ModelPerson createFromParcel(Parcel in) {
            return new ModelPerson(in);
        }

        @Override
        public ModelPerson[] newArray(int size) {
            return new ModelPerson[size];
        }
    };

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(location);
        dest.writeString(status);
        dest.writeString(photo);
        dest.writeByte((byte) (like ? 1 : 0));
    }
}
