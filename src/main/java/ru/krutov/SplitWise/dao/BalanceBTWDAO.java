package ru.krutov.SplitWise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.BalanceBTW;
import ru.krutov.SplitWise.models.Expense_Person;
import ru.krutov.SplitWise.models.Person_Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BalanceBTWDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Person_GroupDAO person_groupDAO;
    public RowMapper<BalanceBTW> rowMapper = new BalanceBTWRowMapper();

    public BalanceBTWDAO(JdbcTemplate jdbcTemplate, Person_GroupDAO person_groupDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.person_groupDAO = person_groupDAO;
    }

    public void addMember(int group_id, String phone) {
       List<Person_Group> group_people = person_groupDAO.showGroupPeople(group_id);
       if (!group_people.isEmpty()) {
           for (Person_Group group_person : group_people) {
               jdbcTemplate.update("INSERT INTO BalanceBTW(group_id,phone,otherphone) VALUES(?,?,?)", group_id, phone, group_person.getPhone());
           }
       }
    }

    public void setDebt(int group_id, List<Expense_Person> exp_people, String paid_by, float amount, int expense_id) {
        for(Expense_Person exp_person :exp_people){
            if (!exp_person.getPhone().equals(paid_by)) {
                BalanceBTW paid_by_balance = findBalance(group_id,paid_by, exp_person.getPhone());
                BalanceBTW debt_balance = findBalance(group_id, exp_person.getPhone(), paid_by);

                paid_by_balance.setBalance(paid_by_balance.getBalance()+exp_person.getAmount());
                debt_balance.setBalance(debt_balance.getBalance()-exp_person.getAmount());

                jdbcTemplate.update("UPDATE BalanceBTW(balance) Values(?) where group_id = ? AND phone = ? AND otherPhone = ?",
                        paid_by_balance, group_id,paid_by,exp_person.getPhone());
                jdbcTemplate.update("UPDATE BalanceBTW(balance) Values(?) where group_id = ? AND phone = ? AND otherPhone = ?",
                        debt_balance, group_id,exp_person.getPhone(),paid_by);
            }
        }
    }
    public BalanceBTW findBalance(int group_id, String phone, String otherPhone){
        return jdbcTemplate.query("Select * FROM BalanceBTW Where group_id = ? And phone = ? And otherPhone = ?"
                , new Object[]{group_id, phone, otherPhone}, rowMapper).stream().findAny().orElse(null);
    }

    public List<BalanceBTW> showPersonBalances(int group_id, String phone) {
        return jdbcTemplate.query("SELECT * FROM BalancesBTW WHERE group_id = ? AND phone = ?", new Object[]{group_id,phone},rowMapper);
    }

    public void manageDebts(int group_id) {
        List<BalanceBTW> balances = jdbcTemplate.query("SELECT * FROM BalanceBTW WHERE group_id = ?", new Object[]{group_id}, rowMapper);
        for (BalanceBTW balance: balances){

        }
    }


    private static class BalanceBTWRowMapper implements RowMapper<BalanceBTW> {

        @Override
        public BalanceBTW mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BalanceBTW(
                    rs.getInt("group_id"),
                    rs.getString("phone"),
                    rs.getString("otherPhone"),
                    rs.getFloat("amount"));
        }
    }

}
