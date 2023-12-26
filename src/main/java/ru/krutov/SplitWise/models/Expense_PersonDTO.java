package ru.krutov.SplitWise.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Expense_PersonDTO {
    private List<Expense_Person> people;

    public Expense_PersonDTO(){
        this.people = new ArrayList<>();
    }

    public Expense_PersonDTO(List<Expense_Person> people) {
        this.people = people;
    }

    public void addPerson(Expense_Person person){
        this.people.add(person);
    }

    public List<Expense_Person> getPeople() {
        return people;
    }

    public void setPeople(List<Expense_Person> people) {
        this.people = people;
    }
}
