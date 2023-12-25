package ru.krutov.SplitWise.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Group;
import ru.krutov.SplitWise.models.Person;
import ru.krutov.SplitWise.models.Person_Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private final RowMapper<Person> rowMapper = new PersonRowMapper();
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO( JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query("Select * From Person", rowMapper);
    }

    public void create(Person person){
        jdbcTemplate.update("INSERT INTO Person(phone,name) Values(?,?)",person.getPhone(),person.getName());
    }

    public Person show(String phone){
        return jdbcTemplate.query("Select * From Person Where phone = ?",
                new Object[]{phone},rowMapper).stream().findAny().orElse(null);
    }
//    public List<Person> showGroupPeople(List<Person_Group> person_groups){
//        List<Person> personList = new ArrayList<>();
//        for(Person_Group person_group: person_groups){
//            personList.add(jdbcTemplate.query("Select * From Person Where phone = ?",
//                            new Object[]{person_group.getPhone()}, rowMapper)
//                    .stream().findAny().orElse(null));
//        }
//        return personList;
//    }
    public void update(String phone, Person person){
        jdbcTemplate.update("Update Person Set phone = ?, name = ? Where phone = ?",person.getPhone(), person.getName(),phone);
    }
    public void delete(String phone){
        jdbcTemplate.update("Delete FROM Person where phone = ?", phone);
    }

    private static class PersonRowMapper implements RowMapper<Person> {

        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Person(
                    rs.getString("phone"),
                    rs.getString("name")
            );
        }
    }
}
