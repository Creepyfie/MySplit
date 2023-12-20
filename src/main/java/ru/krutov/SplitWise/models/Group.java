package ru.krutov.SplitWise.models;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Group {
    private int group_id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be btw 2 and 30 characters")
    private String name;
    public Group(){}

    public Group(int group_id, String name) {
        this.group_id = group_id;
        this.name = name;
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
}
