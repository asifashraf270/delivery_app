package com.example.khushbakht.grocerjin.sugarDB;

import com.orm.SugarRecord;

/**
 * Created by SAG-E-ATTAR on 12/8/2017.
 */

public class InboxTable extends SugarRecord<InboxTable> {

    public boolean isReadFlag() {
        return readFlag;
    }

    public void setReadFlag(boolean readFlag) {
        this.readFlag = readFlag;
    }

    private boolean readFlag ;

    private String notifyMesssage;

    private String notifyTime;



    public String getNotifyMesssage() {
        return notifyMesssage;
    }

    public void setNotifyMesssage(String notifyMesssage) {
        this.notifyMesssage = notifyMesssage;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

}
