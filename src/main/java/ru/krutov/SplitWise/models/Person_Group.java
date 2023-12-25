package ru.krutov.SplitWise.models;

public class Person_Group {
    private String phone;
    private int group_id;

    private String name;

    private float balance;

    public Person_Group(){}

    public Person_Group(int group_id, String phone, String name, float balance) {
        this.phone = phone;
        this.group_id = group_id;
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
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
