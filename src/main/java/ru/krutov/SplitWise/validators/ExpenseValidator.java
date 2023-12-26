package ru.krutov.SplitWise.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.krutov.SplitWise.dao.ExpenseDAO;
import ru.krutov.SplitWise.models.Expense;
@Component
public class ExpenseValidator implements Validator {
    private ExpenseDAO expenseDAO;
    @Autowired
    public ExpenseValidator(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Expense.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    Expense expense = (Expense) target;
    }


}
