package com.bonniepeng.bloomi;

import android.app.Application;

public class Globals extends Application {

    private boolean refreshSettings = false;

    public boolean isRefreshSettings() {
        return refreshSettings;
    }

    public void setRefreshSettings(boolean refreshSettings) {
        this.refreshSettings = refreshSettings;
    }
}
