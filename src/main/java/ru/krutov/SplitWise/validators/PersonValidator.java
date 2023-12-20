package ru.krutov.SplitWise.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.krutov.SplitWise.dao.PersonDAO;
import ru.krutov.SplitWise.models.Person;

@Component
public class PersonValidator implements Validator {

    private PersonDAO personDao;

    @Autowired
    public PersonValidator(PersonDAO personDao){
        this.personDao = personDao;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
    }
}
