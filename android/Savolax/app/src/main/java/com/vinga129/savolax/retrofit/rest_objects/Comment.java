package com.vinga129.savolax.retrofit.rest_objects;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Comment extends RestObject implements Parcelable {
    private Integer customer_id;
    private String content;
    private String created_at;

    private MiniCustomer customer;

    protected Comment(Parcel in) {
        id = in.readInt();
        customer_id = in.readInt();
        content = in.readString();
        created_at = in.readString();
    }

    public Comment(String content) {
        this.content = content;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        if (id != null)
            parcel.writeInt(id);
        parcel.writeInt(customer_id);
        parcel.writeString(content);
        parcel.writeString(created_at);
    }

    public MiniCustomer getCustomer() {
        return customer;
    }

    public Comment withCustomer(MiniCustomer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(final MiniCustomer customer) {
        this.customer = customer;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
