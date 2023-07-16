package it.uniroma3.siwClassical.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwClassical.model.Musician;
import it.uniroma3.siwClassical.service.MusicianService;

@Component
public class MusicianValidator implements Validator {

	@Autowired MusicianService musicianService;


	@Override
	public void validate(Object o, Errors errors) {
		Musician musician = (Musician)o;
		if(musician.getName()!=null && musician.getSurname()!=null && musician.getNationality()!=null
				&& musician.getDateOfBirth() != null && this.musicianService.existsByNameAndSurnameAndNationalityAndDateOfBirth(musician.getName(), musician.getSurname(), musician.getNationality(), musician.getDateOfBirth()))
			errors.reject("musician.duplicate");
		
		if(musician.getDateOfDeath() != null && musician.getDateOfBirth().isAfter(musician.getDateOfDeath()))
			errors.reject("musician.invalidBirthAndDeath");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Musician.class.equals(clazz);
	}

}
