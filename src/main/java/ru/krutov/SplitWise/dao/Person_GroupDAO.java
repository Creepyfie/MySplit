package ru.krutov.SplitWise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Expense;
import ru.krutov.SplitWise.models.Expense_Person;
import ru.krutov.SplitWise.models.Person;
import ru.krutov.SplitWise.models.Person_Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class Person_GroupDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Person_Group> rowMapper = new Person_GroupRowMapper();

    public Person_GroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Person_Group> showPersonGroups(String phone){
        return jdbcTemplate.query("Select * From Person_Group Where phone = ?"
                , new Object[]{phone}, rowMapper);
    }
    public List<Person_Group> showGroupPeople(int group_id){
        return jdbcTemplate.query("Select * From Person_Group Where group_id = ?"
                , new Object[]{group_id}, rowMapper);
    }

    public void removeFromGroup(int group_id, String phone){
        jdbcTemplate.update("DELETE FROM Person_Group Where group_id = ? AND phone = ?",group_id,phone);
    }

    public void addMember(int group_id, Person person){
        jdbcTemplate.update("INSERT INTO Person_Group(group_id,phone,name) VALUES(?,?,?)"
                ,group_id, person.getPhone(),person.getName());
    }

    public void changeBalances(int group_id, List<Expense_Person> exp_people, Expense expense) {

        for(Expense_Person exp_person: exp_people){
            Person_Group person_group = jdbcTemplate.query("SELECT * FROM Person_Group Where group_id = ? And phone = ?",
                    new Object[]{group_id,exp_person.getPhone()},rowMapper).stream().findAny().orElse(null);
            float newBalance = person_group.getBalance();
            if (exp_person.getPhone().equals(expense.getPaid_by()))
            {
                newBalance = newBalance + expense.getAmount();
            }
            newBalance = - exp_person.getAmount();
            jdbcTemplate.update("UPDATE Person_Group(balance) VALUES (?) Where group_id = ? AND phone =?",newBalance,group_id,exp_person.getPhone());
        }
    }

    public Person_Group showBalance(int group_id, String phone) {
        return jdbcTemplate.query("SELECT * FROM Person_Group Where group_id = ? phone = ?",
                new Object[]{group_id,phone},rowMapper).stream().findAny().orElse(null);

    }


    private static class Person_GroupRowMapper implements RowMapper<Person_Group> {

        @Override
        public Person_Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Person_Group(
                    rs.getInt("group_id"),
                    rs.getString("phone"),
                    rs.getString("name"),
                    rs.getFloat("balance"));
        }
    }
}
