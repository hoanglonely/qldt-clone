package com.mb.lab.banks.user.entity;

import java.io.Serializable;

public class DOB implements Serializable {

    private static final long serialVersionUID = 7673623731020093585L;

    private int date;
    private int month;
    private int year;

    public DOB() {
    }

    public DOB(int date, int month, int year) {
        super();
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
