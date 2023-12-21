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
