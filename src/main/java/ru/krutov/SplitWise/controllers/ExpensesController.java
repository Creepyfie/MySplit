package ru.krutov.SplitWise.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.krutov.SplitWise.dao.BalanceBTWDAO;
import ru.krutov.SplitWise.dao.ExpenseDAO;
import ru.krutov.SplitWise.dao.Expense_PersonDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;
import ru.krutov.SplitWise.models.Expense;
import ru.krutov.SplitWise.models.Expense_Person;
import ru.krutov.SplitWise.models.Expense_PersonDTO;
import ru.krutov.SplitWise.models.Person_Group;
import ru.krutov.SplitWise.validators.ExpenseValidator;

import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {
    private ExpenseDAO expenseDAO;
    private Person_GroupDAO person_groupDAO;
    private final Expense_PersonDAO expense_personDAO;
    private final BalanceBTWDAO balanceBTWDAO;

    private final ExpenseValidator expenseValidator;

    @Autowired
    public ExpensesController(ExpenseDAO expenseDAO, Person_GroupDAO person_groupDAO, Expense_PersonDAO expense_personDAO, BalanceBTWDAO balanceBTWDAO, ExpenseValidator expenseValidator) {
        this.expenseDAO = expenseDAO;
        this.person_groupDAO = person_groupDAO;
        this.expense_personDAO = expense_personDAO;
        this.balanceBTWDAO = balanceBTWDAO;
        this.expenseValidator = expenseValidator;
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
      //  model.addAttribute("exp_people",expense_personDAO.showExpensePeople(person_groupDAO.showGroupPeople(group_id)));
        return "expenses/new";
    }
    @PostMapping("/{group_id}")
    public String addExpense(@PathVariable("group_id") int group_id,
                             @ModelAttribute("expense") @Valid Expense expense, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "expenses/new";
        }
        int expense_id = expenseDAO.addExpense(group_id,expense);
        return "redirect:/expenses/manage/"+expense_id;
    }
    @GetMapping("/manage/{expense_id}")
    public String manageExpense(@PathVariable("expense_id") int expense_id,Model model){
        int group_id = expenseDAO.show(expense_id).getGroup_id();
        Expense_PersonDTO exp_people = new Expense_PersonDTO();
        List<Person_Group> group_people = person_groupDAO.showGroupPeople(group_id);
        for(Person_Group person_group: group_people){
            exp_people.addPerson(new Expense_Person());
        }
        model.addAttribute("group_id", group_id);
        model.addAttribute("group_people",group_people);
        model.addAttribute("exp_people",exp_people);
      // model.addAttribute("exp_people",expense_personDAO.showExpensePeople(person_groupDAO.showGroupPeople(group_id)));
        return "expenses/manage";
    }
    @PostMapping("/manage/{group_id}/{expense_id}")
    public String manage(@PathVariable("group_id") int group_id,
                         @PathVariable("expense_id") int expense_id,
                         @ModelAttribute("exp_people") List<Expense_Person> exp_people){
        Expense expense = expenseDAO.show(expense_id);
        expense_personDAO.addExpenses(exp_people, expense_id);
        person_groupDAO.changeBalances(group_id,exp_people, expense);
        balanceBTWDAO.setDebt(group_id,exp_people,expense.getPaid_by()
                ,expense.getAmount(), expense_id);
        return "redirect:groups/";
    }
    @GetMapping("/{expense_id}")
    public String show(@PathVariable("expense_id") int expense_id, Model model) {
        model.addAttribute("expense", expenseDAO.show(expense_id));
        model.addAttribute("expense_people",expense_personDAO.showPeople(expense_id));

        return "expenses/show";
    }
    @GetMapping("/{group_id}/{expense_id}/edit")
    public String edit(@PathVariable("group_id") int group_id,@PathVariable("expense_id") int expense_id, Model model){
        model.addAttribute("expense", expenseDAO.show(expense_id));
        model.addAttribute("exp_people", expense_personDAO.showPeople(expense_id));
        model.addAttribute("group_id",group_id);
        return "expenses/edit";
    }
    @PatchMapping("/{expense_id}/edit")
    public String update(@PathVariable("expense_id") int expense_id,@ModelAttribute("exp_people") List<Expense_Person> exp_people,
                         @ModelAttribute("expense") Expense expense){
        expenseDAO.update(expense_id,expense);
        expense_personDAO.update(expense_id,exp_people);

        return "expenses/edit";
    }

    @DeleteMapping("/{group_id}/{expense_id}")
    public String delete(@PathVariable("expense_id") int expense_id,@PathVariable("group_id") int group_id){
        expenseDAO.delete(expense_id);
        return "redirect:/expenses/"+group_id;
    }
}
