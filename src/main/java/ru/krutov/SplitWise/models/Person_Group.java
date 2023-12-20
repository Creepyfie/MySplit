package ru.krutov.SplitWise.models;

public class Person_Group {
    private String phone;
    private int group_id;

    public Person_Group(){}

    public Person_Group(String phone, int group_id) {
        this.phone = phone;
        this.group_id = group_id;
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
