package ru.krutov.SplitWise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Expense;
import ru.krutov.SplitWise.models.Expense_Person;
import ru.krutov.SplitWise.models.Person_Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Expense_PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Expense_Person> rowMapper = new Expense_PersonRowMapper();


    public Expense_PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Expense_Person> showExpensePeople(List<Person_Group> showGroupPeople) {
        List<Expense_Person> expensePeople = new ArrayList<>();
        for(Person_Group groupPerson :showGroupPeople){
            Expense_Person expensePerson = new Expense_Person();
            expensePerson.setPhone(groupPerson.getPhone());
            expensePerson.setAmount(0f);
            expensePeople.add(expensePerson);
        }
        return  expensePeople;
    }

    public void addExpenses(List<Expense_Person> exp_people, int expense_id) {
        for(Expense_Person exp_person :exp_people){
            jdbcTemplate.update("INSERT INTO Expense_Person(expense_id, phone, amount) VALUES (?,?,?)",
                    expense_id,exp_person.getPhone(),exp_person.getAmount());
        }
    }


    private static class Expense_PersonRowMapper implements RowMapper<Expense_Person> {

        @Override
        public Expense_Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Expense_Person(
                    rs.getInt("expense_id"),
                    rs.getString("phone"),
                    rs.getFloat("amount"));
        }
    }
}
