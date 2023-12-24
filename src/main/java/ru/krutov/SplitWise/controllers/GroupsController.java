package ru.krutov.SplitWise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.krutov.SplitWise.dao.BalanceBTWDAO;
import ru.krutov.SplitWise.dao.GroupDAO;
import ru.krutov.SplitWise.dao.PersonDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;
import ru.krutov.SplitWise.models.Group;
import ru.krutov.SplitWise.models.Person;


@Controller
@RequestMapping("/groups")
public class GroupsController {
    private final GroupDAO groupDAO;
    private final PersonDAO personDAO;
    private final Person_GroupDAO person_groupDAO;

    private final BalanceBTWDAO balanceBTWDAO;

    @Autowired
    public GroupsController(GroupDAO groupDAO, PersonDAO personDAO, Person_GroupDAO person_groupDAO, BalanceBTWDAO balanceBTWDAO) {
        this.groupDAO = groupDAO;
        this.personDAO = personDAO;
        this.person_groupDAO = person_groupDAO;
        this.balanceBTWDAO = balanceBTWDAO;
    }

    @GetMapping()
    public String index(){
        groupDAO.index();
        return "groups/index";
    }
    @GetMapping("/{group_id}")
    public String show(@PathVariable("group_id") int group_id, Model model){
        model.addAttribute("group", groupDAO.show(group_id));
        model.addAttribute("group_people",person_groupDAO.showGroupPeople(group_id));
//        model.addAttribute("people",personDAO.showGroupPeople(person_groupDAO.showGroupPeople(group_id)));
        model.addAttribute("allPeople",personDAO.index());
        return "groups/show";
    }
    @GetMapping("/new")
    public String newGroup(@ModelAttribute("group")Group group){
        return "groups/new";
    }
    @PostMapping()
    public String addGroup(@ModelAttribute("group") Group group){
        int group_id = groupDAO.create(group);
        return ("redirect:/groups/"+group_id);
    }
    @DeleteMapping("/{group_id}/remove/{phone}")
    public String removeMember(@PathVariable("group_id" )int group_id, @PathVariable("phone") String phone){
        person_groupDAO.removeFromGroup(group_id,phone);
        return ("redirect:/groups/"+group_id);
    }
    @PatchMapping("/{group_id}/addMember")
    public String addMember(@ModelAttribute("person")Person person, @PathVariable("group_id") int group_id){
        balanceBTWDAO.addMember(group_id, person.getPhone());
        person_groupDAO.addMember(group_id,person);
        return ("redirect:/groups/"+group_id);
    }
    @DeleteMapping("/{group_id}")
    public String deleteGroup(@PathVariable("group_id") int group_id){
        groupDAO.delete(group_id);
        return "redirect:/groups";
    }
}
