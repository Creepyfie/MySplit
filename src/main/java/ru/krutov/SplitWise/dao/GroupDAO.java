package ru.krutov.SplitWise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krutov.SplitWise.models.Group;
import ru.krutov.SplitWise.models.Person_Group;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GroupDAO {
    private final RowMapper<Group> rowMapper = new GroupRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public GroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Group> index(){
        return jdbcTemplate.query("Select * From Group", rowMapper);
    }

    public Group show(int group_id){
        return jdbcTemplate.query("Select * From Groups Where group_id = ?", new Object[]{group_id},rowMapper)
                .stream().findAny().orElse(null);
    }
    public List<Group> showPersonGroups(List<Person_Group> person_groups){
        List<Group> groupList = new ArrayList<>();
        for(Person_Group person_group: person_groups){
        groupList.add(jdbcTemplate.query("Select * From Group Where group_id = ?",
                new Object[]{person_group.getGroup_id()}, rowMapper)
                .stream().findAny().orElse(null));
        }
        return groupList;
    }

    public void delete(int group_id){
        jdbcTemplate.update("DELETE FROM Groups WHERE group_id = ?", group_id);
    }

    public int create(Group group) {
         jdbcTemplate.update("INSERT INTO Groups(name) VALUES (?)",group.getName());
        return jdbcTemplate.query("Select group_id From Groups Where name = ?",
                new Object[]{group.getName()},rowMapper)
                .stream().findAny().orElse(null).getGroup_id();
    }


    private static class GroupRowMapper implements RowMapper<Group> {

        @Override
        public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Group(
                    rs.getInt("group_id"),
                    rs.getString("name")
            );
        }
    }
}
