package com.bonniepeng.bloomi;

import android.media.Image;

public class Plant {
    private String sciName;
    private String nickname;
    private String metric;
    private String height;
    private String notes;
    private String waterDays;
    private String waterTime; //string??
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plant(String sciName, String nickname, String metric, String height, String notes, String waterDays, String waterTime, int id) {
        this.sciName = sciName;
        this.nickname = nickname;
        this.metric = metric;
        this.height = height;
        this.notes = notes;
        this.waterDays = waterDays;
        this.waterTime = waterTime;
        this.id = id;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWaterDays() {
        return waterDays;
    }

    public void setWaterDays(String waterDays) {
        this.waterDays = waterDays;
    }

    public String getWaterTime() {
        return waterTime;
    }

    public void setWaterTime(String waterTime) {
        this.waterTime = waterTime;
    }

}
