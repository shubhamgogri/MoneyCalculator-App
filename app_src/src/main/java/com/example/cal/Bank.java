package com.example.cal;

public class Bank {
    private String date;
    private String title;
    private String money;

    public Bank(){

    }

    public Bank(String date, String title, String money) {
        this.date = date;
        this.title = title;
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public  void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
