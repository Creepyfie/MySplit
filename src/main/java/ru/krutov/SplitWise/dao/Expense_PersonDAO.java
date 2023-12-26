package ru.krutov.SplitWise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Expense_Person;
import ru.krutov.SplitWise.models.Expense_PersonDTO;
import ru.krutov.SplitWise.models.Person_Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Expense_PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Expense_Person> rowMapper = new Expense_PersonRowMapper();

    private final Person_GroupDAO person_groupDAO;


    public Expense_PersonDAO(JdbcTemplate jdbcTemplate, Person_GroupDAO person_groupDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.person_groupDAO = person_groupDAO;
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
//сделать возможность исключить человека из траты
            jdbcTemplate.update("INSERT INTO Expense_Person(expense_id, phone, amount) VALUES (?,?,?)",
                    expense_id,exp_person.getPhone(),exp_person.getAmount());
        }
    }

    public List<Expense_Person> showPeople(int expense_id) {
        return jdbcTemplate.query("SELECT * FROM Expense_Person Where expense_id = ?", new Object[]{expense_id},rowMapper);
    }

    public void update(int expense_id, List<Expense_Person> exp_people) {
        for(Expense_Person expense_person:exp_people){
            jdbcTemplate.update("UPDATE Expense_Person SET amount = ? WHERE expense_id = ? AND phone = ?"
                    ,expense_person.getAmount(),expense_id,expense_person.getPhone());
        }
    }
    //метод-костыль, поскльку возврашается почишенный список
    public List<Expense_Person> getPeople(int group_id, Expense_PersonDTO expense_personDTO) {
        List<Expense_Person> exp_people = expense_personDTO.getPeople();
        List<Person_Group> group_people = person_groupDAO.showGroupPeople(group_id);
        for(Person_Group person_group: group_people){
            exp_people.
        }
        return exp_people;
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
