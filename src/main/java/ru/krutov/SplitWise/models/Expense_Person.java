package ru.krutov.SplitWise.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Expense_Person {
    private int expense_id;
    private String phone;
    private float amount;

    public Expense_Person(int expense_id, String phone, float amount) {
        this.expense_id = expense_id;
        this.phone = phone;
        this.amount = amount;
    }

    public Expense_Person() {

    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
