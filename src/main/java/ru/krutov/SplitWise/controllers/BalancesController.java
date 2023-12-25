package ru.krutov.SplitWise.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.krutov.SplitWise.dao.BalanceBTWDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;

@Controller
@RequestMapping("/balances")
public class BalancesController {
    private final BalanceBTWDAO balanceBTWDAO;
    private final Person_GroupDAO person_groupDAO;

    public BalancesController(BalanceBTWDAO balanceBTWDAO, Person_GroupDAO person_groupDAO) {
        this.balanceBTWDAO = balanceBTWDAO;
        this.person_groupDAO = person_groupDAO;
    }


    @GetMapping("/{group_id}/{phone}")
    public String indexPerson(@PathVariable("group_id") int group_id, @PathVariable("phone") String phone, Model model){
        model.addAttribute("personBalance", person_groupDAO.showBalance(group_id,phone));
        model.addAttribute("withOther",balanceBTWDAO.showPersonBalances(group_id,phone));
        model.addAttribute("group_people",person_groupDAO.showGroupPeople(group_id));
        return "balances/show";
    }
    @PatchMapping("/{group_id}")
    public String manageDebts(@PathVariable("group_id") int group_id){
        balanceBTWDAO.manageDebts(group_id);
        return "redirect:groups/"+group_id;
    }
}
