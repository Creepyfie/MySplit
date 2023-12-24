package ru.krutov.SplitWise.models;

public class BalanceBTW {
    private int group_id;
    private String phone;
    private String otherPhone;
    private float balance;

    public BalanceBTW(){
    }

    public BalanceBTW(int group_id, String phone, String otherPhone, float balance) {
        this.group_id = group_id;
        this.phone = phone;
        this.otherPhone = otherPhone;
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

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
