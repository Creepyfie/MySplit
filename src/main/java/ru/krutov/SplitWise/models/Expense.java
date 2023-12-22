package ru.krutov.SplitWise.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Expense {

    private int expense_id;
    @NotEmpty
    private int group_id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be btw 2 and 30 characters")
    private String name;
    @NotEmpty(message = "phone should not be empty")
    @Size(min = 10, max = 10, message = "Name should be btw 2 and 30 characters")
    private String paid_by;
    @NotEmpty
    private float amount;

    public Expense(){}

    public Expense(int group_id, int expense_id, String name, String paid_by, float amount) {
        this.expense_id = expense_id;
        this.group_id = group_id;
        this.name = name;
        this.paid_by = paid_by;
        this.amount = amount;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaid_by() {
        return paid_by;
    }

    public void setPaid_by(String paid_by) {
        this.paid_by = paid_by;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
