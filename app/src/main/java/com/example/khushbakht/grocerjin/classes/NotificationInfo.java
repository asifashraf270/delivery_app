package com.example.khushbakht.grocerjin.classes;

/**
 * Created by khush on 11/25/2017.
 */

public class NotificationInfo {

        private String name;
    private String msg;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
        public NotificationInfo()
        {

        }

        public NotificationInfo(String name, String msg, String date)
        {
            this.name = name;
            this.msg = msg;
            this.date = date;
        }
        public void setName(String name)
        {
            this.name = name;
        }
        public void setMsg(String number)
        {
            this.msg = number;
        }
        public String getName()
        {
            return name;
        }
        public String getMsg()
        {
            return msg;
        }
}
