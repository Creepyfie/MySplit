package ru.krutov.SplitWise.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Person show(String phone){
        return jdbcTemplate.query("Select * From Person Where phone = ?",
                new Object[]{phone},rowMapper).stream().findAny().orElse(null);
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
