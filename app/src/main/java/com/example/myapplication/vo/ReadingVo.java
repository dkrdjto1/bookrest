package com.example.myapplication.vo;

import com.example.myapplication.db.sqllite.SQLiteDataBase;

import java.io.Serializable;

public class ReadingVo implements Serializable {
    private String idx;
    private String itemId;
    private String readingdate;
    private String memo;
    private String readingpagenumber;
    private String regDate;
    private String updDate;
    private String alarm;

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getReadingdate() {
        return readingdate;
    }

    public void setReadingdate(String readingdate) {
        this.readingdate = readingdate;
    }

    public String getReadingpagenumber() {
        return readingpagenumber;
    }

    public void setReadingpagenumber(String readingpagenumber) {
        this.readingpagenumber = readingpagenumber;
    }
}
