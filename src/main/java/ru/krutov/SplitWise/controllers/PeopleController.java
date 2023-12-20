package ru.krutov.SplitWise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.krutov.SplitWise.dao.GroupDAO;
import ru.krutov.SplitWise.dao.PersonDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;
import ru.krutov.SplitWise.validators.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;
    private final Person_GroupDAO person_groupDAO;
    private final GroupDAO groupDAO;
    private final PersonValidator personValidator;
    @Autowired
    public PeopleController(PersonDAO personDAO, GroupDAO groupDAO, Person_GroupDAO person_groupDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.person_groupDAO = person_groupDAO;
        this.groupDAO = groupDAO;
        this.personValidator = personValidator;
    }
    @GetMapping
    public String index(Model model){
        model.addAttribute("people",personDAO.index());
        return "people/index";
    }
    @GetMapping("/{phone}")
    public String show(@PathVariable("phone") String phone, Model model){
    model.addAttribute("person", personDAO.show(phone));
    model.addAttribute("groups",
            groupDAO.showPersonGroups(person_groupDAO.showPersonGroups(phone)));

        return "people/show";
    }


}
