package ru.krutov.SplitWise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.krutov.SplitWise.dao.ExpenseDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;
import ru.krutov.SplitWise.models.Expense;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {
    private ExpenseDAO expenseDAO;
    private Person_GroupDAO person_groupDAO;

    @Autowired
    public ExpensesController(ExpenseDAO expenseDAO, Person_GroupDAO person_groupDAO) {
        this.expenseDAO = expenseDAO;
        this.person_groupDAO = person_groupDAO;
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
        return "expenses/new";
    }
}
