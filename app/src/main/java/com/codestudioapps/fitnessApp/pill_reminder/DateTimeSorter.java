


package com.codestudioapps.fitnessApp.pill_reminder;

// Class to create DateTime objects for easy sorting
public class DateTimeSorter {
    public int mIndex;
    public String mDateTime;


    public DateTimeSorter(int index, String DateTime){
        mIndex = index;
        mDateTime = DateTime;
    }

    public DateTimeSorter(){}


    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }
}
