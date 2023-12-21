package ru.krutov.SplitWise.models;

public class Person_Group {
    private String phone;
    private int group_id;

    private String name;
    private double balance;

    public Person_Group(){}

    public Person_Group(int group_id, String phone, String name, double balance) {
        this.phone = phone;
        this.group_id = group_id;
        this.name = name;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
