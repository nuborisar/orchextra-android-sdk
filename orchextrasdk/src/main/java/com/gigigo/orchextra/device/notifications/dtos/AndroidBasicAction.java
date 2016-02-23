package com.gigigo.orchextra.device.notifications.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class AndroidBasicAction implements Parcelable {

    private String action;
    private String url;
    private AndroidNotification notification;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AndroidNotification getNotification() {
        return notification;
    }

    public void setNotification(AndroidNotification notification) {
        this.notification = notification;
    }

    @Override
    public int hashCode() {
        int megaHash = 0;
        if (this.getNotification() != null) {
            megaHash = megaHash + hashCodeObject(this.getNotification().getTitle());
            megaHash = megaHash + hashCodeObject(this.getNotification().getBody());
        }
        megaHash = megaHash + hashCodeObject(this.getAction());
        megaHash = megaHash + hashCodeObject(this.getUrl());

        return megaHash;
    }

    public static int hashCodeObject(Object o) {
        return (o == null) ? 0 : o.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.url);
        dest.writeParcelable(this.notification, 0);
    }

    public AndroidBasicAction() {
    }

    protected AndroidBasicAction(Parcel in) {
        this.action = in.readString();
        this.url = in.readString();
        this.notification = in.readParcelable(AndroidNotification.class.getClassLoader());
    }

    public static final Parcelable.Creator<AndroidBasicAction> CREATOR = new Parcelable.Creator<AndroidBasicAction>() {
        public AndroidBasicAction createFromParcel(Parcel source) {
            return new AndroidBasicAction(source);
        }

        public AndroidBasicAction[] newArray(int size) {
            return new AndroidBasicAction[size];
        }
    };
}
