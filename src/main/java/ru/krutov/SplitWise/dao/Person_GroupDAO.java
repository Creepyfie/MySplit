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

    public void changeBalances(List<Expense_Person> exp_people, Expense expense) {
    }


    private static class Person_GroupRowMapper implements RowMapper<Person_Group> {

        @Override
        public Person_Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Person_Group(
                    rs.getInt("group_id"),
                    rs.getString("phone"),
                    rs.getString("name"),
                    rs.getDouble("balance"));
        }
    }
}
