package it.uniroma3.siwClassical.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwClassical.model.Comment;
import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.model.Image;
import it.uniroma3.siwClassical.model.Musician;
import it.uniroma3.siwClassical.model.Review;
import it.uniroma3.siwClassical.repository.CommentRepository;
import it.uniroma3.siwClassical.repository.EventRepository;
import it.uniroma3.siwClassical.repository.GroupRepository;
import it.uniroma3.siwClassical.repository.ImageRepository;
import it.uniroma3.siwClassical.repository.MusicianRepository;
import it.uniroma3.siwClassical.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class EventService {

	@Autowired EventRepository eventRepository;

	@Autowired ImageRepository imageRepository;

	@Autowired MusicianRepository musicianRepository;

	@Autowired UserService userService;

	@Autowired ReviewRepository reviewRepository;

	@Autowired GroupRepository groupRepository;

	@Autowired  CommentRepository commentRepository;


	public Iterable<Event> findAllEvents(){
		return this.eventRepository.findAll();
	}

	public Iterable<Event> findAllEventsOrderByDate(){
		return this.eventRepository.findAllOrderByDate();
	}

	public Event findEventById(Long id) {
		return this.eventRepository.findById(id).orElse(null);
	}

	public boolean existEventByNameAndLocationAndCityAndDateEvent(String name, String location, String City, LocalDateTime dateEvent) {
		return this.eventRepository.existsByNameAndLocationAndCityAndDateEvent(name, location, City, dateEvent);
	}

	@Transactional
	public Event createNewEvent(Event event, MultipartFile poster) throws IOException {

		event.setPoster(this.imageRepository.save(new Image(poster.getName(),poster.getBytes())));


		return this.eventRepository.save(event);
	}

	@Transactional
	public Event saveSolistToEvent(Long idSoloist, Long idEvent) {
		Event result = null;
		Musician soloist = this.musicianRepository.findById(idSoloist).orElse(null);
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		if(soloist!=null && event!=null) {
			event.setSoloist(soloist);
			this.eventRepository.save(event);
			result=event;
		}
		return result;
	}


	@Transactional
	public Event saveOrchestraDirectorToEvent(Long idOrchestraDirector, Long idEvent) {
		Event result = null;
		Musician orchestraDirector = this.musicianRepository.findById(idOrchestraDirector).orElse(null);
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		if(orchestraDirector!=null && event!=null) {
			event.setOrchestraDirector(orchestraDirector);
			this.eventRepository.save(event);
			result=event;
		}
		return result;
	}


	@Transactional
	public Event removeSoloistFromEvent(Long idSoloist, Long idEvent) {
		Event result = null;
		Musician soloist = this.musicianRepository.findById(idSoloist).orElse(null);
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		if(soloist!=null && event!=null) {
			event.setSoloist(null);
			this.eventRepository.save(event);
			result=event;
		}
		return result;
	}

	@Transactional
	public Event removeOrchestraDirectorFromEvent(Long idOrchestraDirector, Long idEvent) {
		Event result = null;
		Musician soloist = this.musicianRepository.findById(idOrchestraDirector).orElse(null);
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		if(soloist!=null && event!=null) {
			event.setOrchestraDirector(null);
			this.eventRepository.save(event);
			result=event;
		}
		return result;
	}



	@Transactional
	public Event saveOrchestraMemberToEvent(Long idMusician, Long idEvent) {
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		Musician musician = this.musicianRepository.findById(idMusician).orElse(null);
		if(event !=null && musician != null) {
			event.getOrchestra().add(musician);
			this.eventRepository.save(event);
		}
		return event;
	}



	@Transactional
	public Event removeOrchestraMemberFromEvent(Long idMusician, Long idEvent) {
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		Musician musician = this.musicianRepository.findById(idMusician).orElse(null);
		if(event !=null && musician != null) {
			event.getOrchestra().remove(musician);
			this.eventRepository.save(event);
		}
		return event;
	}




	@Transactional
	public void removeEvent(Long idEvent) {
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		Musician soloist = event.getSoloist();
		Musician director = event.getOrchestraDirector();
		Image poster = event.getPoster();

		if(poster!=null)
			this.imageRepository.delete(poster);

		if(soloist!=null)
			soloist.getPlayedSoloist().remove(event);

		if(director!=null)
			director.getDirectedOrchestra().remove(event);

		Set<Musician> memberOrchestra = event.getOrchestra();

		for(Musician m: memberOrchestra)
			m.getPlayedInOrchestra().remove(event);


		//elimina i gruppi ed i commenti presenti nei gruppi
		Set<Group> group = event.getGroups();
		for(Group g : group) {
			for(Comment c : g.getComments()) {
				if(c!=null)
					this.commentRepository.delete(c);
			}
		}
		for(Group g : group) {
			g.setComments(null);
			event.getGroups().remove(g);
			this.groupRepository.delete(g);
		}
		event.setGroups(null);


		//elimina le reviews collegate all'evento
		for(Review r : event.getReviews()) {
			this.reviewRepository.delete(r);
		}
		event.setReviews(null);


		this.eventRepository.delete(event);
	}


	@Transactional
	public Event newReviewToEvent(Review review, Long idEvent) {
		Event event = this.eventRepository.findById(idEvent).orElse(null);

		review.setAuthor(this.userService.getCurrentUser());
		review.setEventReviewed(event);

		event.getReviews().add(review);

		this.reviewRepository.save(review);
		this.eventRepository.save(event);

		return event;

	}


	@Transactional
	public Event updatePosterToEvent(Long idEvent, MultipartFile fileUpload) throws IOException{
		Event event = this.eventRepository.findById(idEvent).orElse(null);

		Image poster = event.getPoster();

		if(!fileUpload.isEmpty()) {
			if(poster==null)
				event.setPoster(new Image(fileUpload.getName(),fileUpload.getBytes()));
			else {
				poster.setName(fileUpload.getName());
				poster.setData(fileUpload.getBytes());
			}
		}
		return this.eventRepository.save(event);
	}

	@Transactional
	public Event saveEvent(Event event) {
		return this.eventRepository.save(event);
	}


}
