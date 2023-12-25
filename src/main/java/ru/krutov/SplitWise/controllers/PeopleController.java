package ru.krutov.SplitWise.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.krutov.SplitWise.dao.GroupDAO;
import ru.krutov.SplitWise.dao.PersonDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;
import ru.krutov.SplitWise.models.Person;
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
    @GetMapping()
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
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") Person person){
        personDAO.create(person);
        return "redirect:/people";
    }
    @GetMapping("{phone}/edit")
    public String edit(@PathVariable ("phone") String phone, Model model){
        model.addAttribute("person",personDAO.show(phone));
        return "people/edit";
    }
    @PatchMapping("{phone}/edit")
    public String update(@PathVariable("phone") String phone,
                         @ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person,bindingResult);
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        personDAO.update(phone, person);
        return "redirect:/people";
    }
    @DeleteMapping("{phone}")
    public String delete(@PathVariable("phone") String phone){
        personDAO.delete(phone);
        return "redirect:/people";
    }


}
