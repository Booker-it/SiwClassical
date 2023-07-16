package it.uniroma3.siwClassical.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.service.GroupService;

@Component
public class GroupValidator implements Validator {


	@Autowired GroupService groupService;

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Group group = (Group)target;
		if(group != null && group.getName()!=null && this.groupService.existsGroupByNameAndGroupCreator(group.getName(), group.getGroupCreator()))
			errors.reject("group.duplicate");


	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Group.class.equals(clazz);
	}
}
