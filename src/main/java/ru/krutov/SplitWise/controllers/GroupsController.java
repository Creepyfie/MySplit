package ru.krutov.SplitWise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.krutov.SplitWise.dao.GroupDAO;
import ru.krutov.SplitWise.dao.PersonDAO;
import ru.krutov.SplitWise.dao.Person_GroupDAO;


@Controller
@RequestMapping("/groups")
public class GroupsController {
    private final GroupDAO groupDAO;
    private final PersonDAO personDAO;
    private final Person_GroupDAO person_groupDAO;

    @Autowired
    public GroupsController(GroupDAO groupDAO, PersonDAO personDAO, Person_GroupDAO person_groupDAO) {
        this.groupDAO = groupDAO;
        this.personDAO = personDAO;
        this.person_groupDAO = person_groupDAO;
    }

    @GetMapping()
    public String index(){
        groupDAO.index();
        return "groups/index";
    }
    @GetMapping("/{group_id}")
    public String show(@PathVariable("group_id") int group_id, Model model){
        model.addAttribute("group", groupDAO.show(group_id));
        model.addAttribute("people",personDAO.showGroupPeople(person_groupDAO.showGroupPeople(group_id)));
        model.addAttribute("allpeople",personDAO.index());
        return "groups/show";
    }
    @DeleteMapping("/{group_id}/remove/{phone}")
    public String removeMember(@PathVariable("group_id" )int group_id, @PathVariable("phone") String phone){
        person_groupDAO.removeFromGroup(group_id,phone);
        return ("redirect:/groups/{group_id}");
    }
}
