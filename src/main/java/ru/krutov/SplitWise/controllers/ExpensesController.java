package ru.krutov.SplitWise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.krutov.SplitWise.dao.ExpenseDAO;
import ru.krutov.SplitWise.dao.Expense_PersonDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;
import ru.krutov.SplitWise.models.Expense;
import ru.krutov.SplitWise.models.Expense_Person;

import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {
    private ExpenseDAO expenseDAO;
    private Person_GroupDAO person_groupDAO;
    private final Expense_PersonDAO expense_personDAO;

    @Autowired
    public ExpensesController(ExpenseDAO expenseDAO, Person_GroupDAO person_groupDAO, Expense_PersonDAO expense_personDAO) {
        this.expenseDAO = expenseDAO;
        this.person_groupDAO = person_groupDAO;
        this.expense_personDAO = expense_personDAO;
    }

    @GetMapping("/{group_id}")
    public String index(@PathVariable("group_id") int group_id, Model model){
        model.addAttribute("expenses", expenseDAO.index(group_id));
        model.addAttribute("group_id",group_id);
        return "expenses/index";
    }
    @GetMapping("/{group_id}/add")
    public String create(@PathVariable("group_id") int group_id, Model model){
        model.addAttribute("expense", new Expense());
        model.addAttribute("group_id",group_id);
        model.addAttribute("group_people",person_groupDAO.showGroupPeople(group_id));
        model.addAttribute("exp_people",expense_personDAO.showExpensePeople(person_groupDAO.showGroupPeople(group_id)));
        return "expenses/new";
    }
    @PostMapping("/{group_id}")
    public String addExpense(@PathVariable("group_id") int group_id,
                             @ModelAttribute("exp_people") List<Expense_Person> exp_people,
                             @ModelAttribute("expense") Expense expense){
        int expense_id = expenseDAO.addExpense(group_id,expense);
        expense_personDAO.addExpenses(exp_people, expense_id);
        person_groupDAO.changeBalances(exp_people, expense);

        return ("redirect:/expenses/"+group_id);
    }
}
