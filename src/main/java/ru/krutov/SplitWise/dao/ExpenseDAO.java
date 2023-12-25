package ru.krutov.SplitWise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Expense;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ExpenseDAO {
    private final RowMapper<Expense> rowMapper = new ExpenseRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public ExpenseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Expense> index(int group_id){
        return jdbcTemplate.query("Select * From Expense Where group_id = ?",
                new Object[]{group_id},rowMapper);
    }

    public int addExpense(int group_id, Expense expense) {
        jdbcTemplate.update("INSERT INTO Expense(group_id,name,paid_by,amount) VALUES (?,?,?,?)",
                group_id, expense.getName(),expense.getPaid_by(),expense.getAmount());
        return jdbcTemplate.query("Select * From Expense Where name = ?",
                new Object[]{expense.getName()},rowMapper).stream().findAny().orElse(null).getExpense_id();
    }

    public Expense show(int expense_id) {
        return jdbcTemplate.query("SELECT * FROM Expense Where expense_id = ?",
                new Object[]{expense_id},rowMapper).stream().findAny().orElse(null);
    }

    public void delete(int expense_id) {
        jdbcTemplate.update("DELETE FROM Expense Where expense_id = ?", expense_id);
    }

    public void update(int expense_id, Expense expense) {
        jdbcTemplate.update("UPDATE Expense SET name =?, paid_by = ?, amount = ? WHERE expense_id = ?"
                ,expense.getName(),expense.getPaid_by(),expense.getAmount(),expense_id);
    }

    private static class ExpenseRowMapper implements RowMapper<Expense> {

        @Override
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Expense(
                    rs.getInt("group_id"),
                    rs.getInt("expense_id"),
                    rs.getString("name"),
                    rs.getString("paid_by"),
                    rs.getFloat("amount")
            );
        }
    }
}
