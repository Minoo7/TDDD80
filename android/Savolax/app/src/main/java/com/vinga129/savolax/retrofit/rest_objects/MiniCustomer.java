package com.vinga129.savolax.retrofit.rest_objects;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class MiniCustomer extends RestObject implements Parcelable {
    private String username;
    private String imageUrl;

    protected MiniCustomer(Parcel in) {
        username = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<MiniCustomer> CREATOR = new Creator<MiniCustomer>() {
        @Override
        public MiniCustomer createFromParcel(Parcel in) {
            return new MiniCustomer(in);
        }

        @Override
        public MiniCustomer[] newArray(int size) {
            return new MiniCustomer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(username);
        parcel.writeString(imageUrl);
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
