package it.uniroma3.siwClassical.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Image;
import it.uniroma3.siwClassical.model.Musician;
import it.uniroma3.siwClassical.repository.EventRepository;
import it.uniroma3.siwClassical.repository.ImageRepository;
import it.uniroma3.siwClassical.repository.MusicianRepository;
import jakarta.transaction.Transactional;

@Service
public class MusicianService {
	@Autowired MusicianRepository musicianRepository;
	
	@Autowired EventRepository eventRepository;
	
	@Autowired ImageRepository imageRepository;
	
	public boolean existsByNameAndSurnameAndNationalityAndDateOfBirth(String name, String surname, String nationality, LocalDate nascita){
		return this.musicianRepository.existsByNameAndSurnameAndNationalityAndDateOfBirth(name, surname, nationality, nascita);
	}
	
	public Iterable<Musician> findAllMusicians(){
		return this.musicianRepository.findAll();
	}
	
	public Musician findMusicianById(Long id) {
		return this.musicianRepository.findById(id).orElse(null);
	}
	
	public List<Musician> findMusciansNotInEvent(Long idEvent){
		
		List<Musician> musiciansToAdd = new ArrayList<>();
		
		for(Musician m : this.musicianRepository.findAllByPlayedInOrchestraIsNotContaining(this.eventRepository.findById(idEvent).orElse(null))) {
			musiciansToAdd.add(m);
		}
		return musiciansToAdd;
	}
	
	
	@Transactional
	public Musician createNewMusician(Musician musician, MultipartFile photo) throws IOException {
		musician.setPhoto(this.imageRepository.save(new Image(photo.getName(),photo.getBytes())));
		return this.musicianRepository.save(musician);
	}
	
	
	@Transactional
	public void removeMusician(Long idMusician) {
		Musician musician = this.musicianRepository.findById(idMusician).orElse(null);
		
		for(Event event : musician.getDirectedOrchestra()) {
			if(event.getOrchestraDirector().equals(musician))
				event.setOrchestraDirector(null);
		}
		
		for(Event event : musician.getPlayedSoloist()) {
			if(event.getSoloist().equals(musician))
				event.setSoloist(null);
		}
		
		for(Event event : musician.getPlayedInOrchestra()) {
			event.getOrchestra().remove(musician);
		}
		
		
		this.musicianRepository.delete(musician);
	}

	
	@Transactional
	public Musician updatePhotoToMusician(Long idMusician, MultipartFile file) throws IOException{
		Musician musician = this.findMusicianById(idMusician);
		Image photo = musician.getPhoto();
		
		if(photo==null) {
			musician.setPhoto(new Image(file.getName(),file.getBytes()));
		}
		else {
			photo.setName(file.getName());
			photo.setData(file.getBytes());
		}
		
		return this.musicianRepository.save(musician);
		
	}
	
	
	@Transactional
	public Musician changeDateOfDeath(Long idMusician, LocalDate dateOfDeath) {
		Musician musician = this.musicianRepository.findById(idMusician).orElse(null);
		
		if(musician!=null && !musician.getDateOfBirth().isAfter(dateOfDeath)) {
			musician.setDateOfDeath(dateOfDeath);
		}
		return this.musicianRepository.save(musician);
	}
	
	
	@Transactional
	public void removeDateOfDeath(Long idMusician) {
		Musician musician = this.musicianRepository.findById(idMusician).orElse(null);
		
		if(musician!=null) {
			musician.setDateOfDeath(null);
		}
		this.musicianRepository.save(musician);
	}
}
