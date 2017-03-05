package com.ktselvi.inspireme.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class Author implements Parcelable {

    private String name;
    private String profile_picture;

    public Author() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(profile_picture);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public Author(Parcel in) {
        name = in.readString();
        profile_picture = in.readString();
    }
}
