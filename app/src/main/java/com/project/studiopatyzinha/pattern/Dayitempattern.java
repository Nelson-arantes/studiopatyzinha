package com.project.studiopatyzinha.pattern;

public class Dayitempattern {
    String dayweek;
    String daymonth;
    String ifselected;

    public String getQuantagend() {
        return quantagend;
    }

    public void setQuantagend(String quantagend) {
        this.quantagend = quantagend;
    }

    String quantagend;
    String month, year;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIfselected() {
        return ifselected;
    }

    public void setIfselected(String ifselected) {
        this.ifselected = ifselected;
    }

    public Dayitempattern(String dayweek, String daymonth, String ifselected, String month, String year, String quantagend) {
        this.dayweek = dayweek;
        this.daymonth = daymonth;
        this.ifselected = ifselected;
        this.year = year;
        this.month = month;
        this.quantagend = quantagend;

    }

    public String getBackcolor() {
        return ifselected;
    }

    public void setBackcolor(String backcolor) {
        this.ifselected = backcolor;
    }

    public String getDayweek() {
        return dayweek;
    }

    public void setDayweek(String dayweek) {
        this.dayweek = dayweek;
    }

    public String getDaymonth() {
        return daymonth;
    }

    public void setDaymonth(String daymonth) {
        this.daymonth = daymonth;
    }
}
