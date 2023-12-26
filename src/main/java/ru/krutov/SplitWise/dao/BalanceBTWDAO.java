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
import java.util.stream.Collectors;

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
               jdbcTemplate.update("INSERT INTO BalanceBTW(group_id,phone,otherphone) VALUES(?,?,?)", group_id, group_person.getPhone(),phone);
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
        return jdbcTemplate.query("SELECT * FROM BalanceBTW WHERE group_id = ? AND phone = ?", new Object[]{group_id,phone},rowMapper);
    }

    public void manageDebts(int group_id) {
        List<Person_Group> group_people = person_groupDAO.showGroupPeople(group_id);

        for(Person_Group person_group: group_people){
            List<BalanceBTW> balances = jdbcTemplate.query("SELECT * FROM BalanceBTW WHERE group_id = ?", new Object[]{group_id}, rowMapper);

            List<BalanceBTW> personBalances = balances.stream().filter(p -> p.getPhone() == person_group.getPhone())
                                                .collect(Collectors.toList());
            List<BalanceBTW> positiveBalances = personBalances.stream()
                                            .filter(p -> p.getBalance() > 0)
                                            .collect(Collectors.toList());
            List<BalanceBTW> negativeBalances = personBalances.stream()
                                            .filter(p -> p.getBalance() < 0)
                                            .collect(Collectors.toList());
            for(BalanceBTW positive: positiveBalances){
                for(BalanceBTW negative: negativeBalances){
                    BalanceBTW posNegBal = twoPersonsBalance(balances,positive.getOtherPhone(),negative.getOtherPhone());
                    BalanceBTW negPosBal = twoPersonsBalance(balances,negative.getOtherPhone(),positive.getOtherPhone());
                    BalanceBTW posPerBal = twoPersonsBalance(balances,positive.getOtherPhone(),positive.getPhone());
                    BalanceBTW negPerBal = twoPersonsBalance(balances,negative.getOtherPhone(),negative.getPhone());
                     float difference;
                    if(positive.getBalance() + negative.getBalance()>= 0){
                        difference = - negative.getBalance();
                                    positive.setBalance(positive.getBalance()-difference);
                                    negative.setBalance(0f);
                                    posPerBal.setBalance(posPerBal.getBalance()+difference);
                                    negPerBal.setBalance(0f);
                        }
                        else{
                        difference = positive.getBalance();
                            negative.setBalance(negative.getBalance()+difference);
                            positive.setBalance(0f);
                            negPerBal.setBalance(negPerBal.getBalance()-difference);
                            posPerBal.setBalance(0f);
                        }
                        posNegBal.setBalance(posNegBal.getBalance()-difference);
                        negPosBal.setBalance(negPosBal.getBalance()+difference);

                    jdbcTemplate.update("UPDATE BalanceBTW SET balance = ? WHERE group_id=? AND phone = ? AND otherPhone = ?"
                            , positive.getBalance(),group_id, positive.getPhone(), positive.getOtherPhone());
                    jdbcTemplate.update("UPDATE BalanceBTW SET balance = ? WHERE group_id=? AND phone = ? AND otherPhone = ?"
                            , posPerBal.getBalance(),group_id, posPerBal.getPhone(), posPerBal.getOtherPhone());
                    jdbcTemplate.update("UPDATE BalanceBTW SET balance = ? WHERE group_id=? AND phone = ? AND otherPhone = ?"
                            , negative.getBalance(),group_id, negative.getPhone(), negative.getOtherPhone());
                    jdbcTemplate.update("UPDATE BalanceBTW SET balance = ? WHERE group_id=? AND phone = ? AND otherPhone = ?"
                            , negPerBal.getBalance(),group_id, negPerBal.getPhone(), negPerBal.getOtherPhone());

                    jdbcTemplate.update("UPDATE BalanceBTW SET balance = ? WHERE group_id=? AND phone = ? AND otherPhone = ?"
                            , posNegBal.getBalance(),group_id, posNegBal.getPhone(), posNegBal.getOtherPhone());
                    jdbcTemplate.update("UPDATE BalanceBTW SET balance = ? WHERE group_id=? AND phone = ? AND otherPhone = ?"
                            , negPosBal.getBalance(),group_id, negPosBal.getPhone(), negPosBal.getOtherPhone());

                }
            }


        }

    }
    public BalanceBTW twoPersonsBalance(List<BalanceBTW> balancesBTW, String phone, String otherPhone){
            return balancesBTW.stream()
                    .filter(p -> p.getPhone().equals(phone))
                    .filter(p -> p.getPhone().equals(otherPhone))
                    .findAny().orElse(null);
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
