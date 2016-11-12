package com.example.android.newsapp;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Ramesh on 10/28/2016.
 */

public class News implements Parcelable {

    private String mSection;
    private String mTitle;
    private String mDate;
    private String mUrl;

    public News (String section, String title, String date, String url) {
        this.mSection = section;
        this.mTitle = title;
        this.mDate = date;
        this.mUrl = url;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    //Implements Parcelable Methods
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mSection);
        out.writeString(mTitle);
        out.writeString(mDate);
        out.writeString(mUrl);
    }

    public static final Parcelable.Creator<News> CREATOR
            = new Parcelable.Creator<News>() {

        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

    private News(Parcel in) {
        mSection = in.readString();
        mTitle = in.readString();
        mDate = in.readString();
        mUrl = in.readString();
    }


}// class News