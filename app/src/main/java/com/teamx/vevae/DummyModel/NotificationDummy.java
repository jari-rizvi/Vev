package com.teamx.vevae.DummyModel;

import java.util.List;

public class NotificationDummy {


    String notificationDescription;
    String notificationTime;
    int notificationImage;
    List<String> boldWords;


    boolean isOpened = true;


    public NotificationDummy(String notificationDescription, String notificationTime, int notificationImage, List<String> boldWords) {
        this.notificationDescription = notificationDescription;
        this.notificationTime = notificationTime;
        this.notificationImage = notificationImage;
        this.boldWords = boldWords;
    }

    public List<String> getBoldWords() {
        return boldWords;
    }

    public void setBoldWords(List<String> boldWords) {
        this.boldWords = boldWords;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public int getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(int notificationImage) {
        this.notificationImage = notificationImage;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
