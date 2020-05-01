package com.luminous.pdi.calendar.dao;


import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Dhaval Soneji on 13/5/16.
 */
public class Event  {
    private String date;
    private String count;
    private ArrayList<EventData> eventData;

/*
    public Event(Calendar day) {
        super(day);
    }
*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<EventData> getEventData() {
        return eventData;
    }

    public void setEventData(ArrayList<EventData> eventData) {
        this.eventData = eventData;
    }
}
