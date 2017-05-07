package com.islavstan.lunahod.utils;


import com.boontaran.DataManager;

//аналог shared preference
public class Data {
    private DataManager manager;
    private static final String PREFERENCE_NAME = "lunahod_data";
    private static final String PROGRESS_KEY = "progress";

    public Data() {
        manager = new DataManager(PREFERENCE_NAME);

    }

    public int getProgress() {
        return manager.getInt(PROGRESS_KEY, 1); //по умолчанию берём 1 уровень
    }

    public void setProgress(int progress){
        manager.saveInt(PROGRESS_KEY, progress);
    }


}
