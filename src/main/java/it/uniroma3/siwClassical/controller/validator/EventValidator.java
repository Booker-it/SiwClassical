package it.uniroma3.siwClassical.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.service.EventService;

@Component
public class EventValidator implements Validator {
	
	@Autowired
	private EventService eventService;
	
	
	@Override
	public void validate(Object target, Errors errors) {
		Event event = (Event)target;
		
		if(event.getName() !=null && event.getLocation()!=null && event.getCity() != null && event.getDateEvent() !=null
				&& this.eventService.existEventByNameAndLocationAndCityAndDateEvent(event.getName(), event.getLocation(), event.getCity(), event.getDateEvent()))
			errors.reject("event.duplicate");
		
		
	}
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return EventValidator.class.equals(clazz);
	}

}
