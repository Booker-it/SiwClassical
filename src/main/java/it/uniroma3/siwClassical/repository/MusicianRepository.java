package it.uniroma3.siwClassical.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Musician;

@Repository
public interface MusicianRepository extends CrudRepository<Musician, Long> {

	boolean existsByNameAndSurnameAndNationalityAndDateOfBirth(String name, String surname, String nationality, LocalDate nascita);
	
	public List<Musician> findAllByPlayedInOrchestraIsNotContaining(Event event);
	

}
