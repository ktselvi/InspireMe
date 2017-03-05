package com.ktselvi.inspireme.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class Quote implements Parcelable {

    public Quote(){

    }

    private String quote;
    private String author;
    private String tag;

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quote);
        parcel.writeString(author);
        parcel.writeString(tag);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public Quote(Parcel in) {
        quote = in.readString();
        author = in.readString();
        tag = in.readString();
    }
}
