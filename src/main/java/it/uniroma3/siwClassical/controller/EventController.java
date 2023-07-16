package it.uniroma3.siwClassical.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siwClassical.controller.utility.FileUploadUtil;
import it.uniroma3.siwClassical.controller.validator.EventValidator;
import it.uniroma3.siwClassical.controller.validator.FileValidator;
import it.uniroma3.siwClassical.controller.validator.ReviewValidator;
import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Musician;
import it.uniroma3.siwClassical.model.Review;
import it.uniroma3.siwClassical.service.EventService;
import it.uniroma3.siwClassical.service.MusicianService;
import it.uniroma3.siwClassical.service.UserService;
import jakarta.validation.Valid;

@Controller
public class EventController {

	@Autowired 
	private EventService eventService;

	@Autowired 
	private FileValidator fileValidator;

	@Autowired
	private EventValidator eventValidator;
	
	@Autowired
	private MusicianService musicianService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReviewValidator reviewValidator;

	@GetMapping(value= "/event")
	public String getEvents(Model model) {
		model.addAttribute("events",this.eventService.findAllEventsOrderByDate());
		return "/events.html";
	}

	@GetMapping("/event/{id}")
	public String getEvent(@PathVariable("id") Long id,Model model) {
		Event event = this.eventService.findEventById(id);
		if(event !=null) {
			model.addAttribute("event",event);
			
			model.addAttribute("user",this.userService.getCurrentUser()); // aggiunto per far eliminare all'utente la propria recensione
			
			return "event.html";
		}
		else
			return "/errors/eventError.html";
	}

	@GetMapping(value="/admin/indexEvent")
	public String indexEvent(Model model) {
		return "admin/indexEvent.html";
	}

	@GetMapping(value="/admin/manageEvents")
	public String manageEvents(Model model) {
		model.addAttribute("events", this.eventService.findAllEvents());
		return "admin/manageEvents.html";
	}

	@GetMapping(value="/admin/formNewEvent")
	public String formNewEvent(Model model) {
		model.addAttribute("event", new Event());
		return "admin/formNewEvent.html";
	}

	@PostMapping("/admin/event")
	public String newEvent(@Valid @ModelAttribute("event") Event event, BindingResult bindingResultEvent, 
			@Valid @ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResultFileUpload, Model model) {

		this.eventValidator.validate(event, bindingResultEvent);
		this.fileValidator.validate(fileUpload, bindingResultFileUpload);

		if(!bindingResultEvent.hasErrors() && !bindingResultFileUpload.hasErrors()) {
			try {
				model.addAttribute("event",this.eventService.createNewEvent(event,fileUpload.getImage()));
				return "redirect:/event/" + event.getId();

			}
			catch(IOException e){
				return "/errors/newEventError.html";
				
			}
		}
		return "admin/formNewEvent.html";
	}


	@GetMapping(value="/admin/formUpdateEvent/{id}")
	public String formUpdateEvent(@PathVariable("id") Long id, Model model) {
		Event event = this.eventService.findEventById(id);
		if(event !=null)
			model.addAttribute("event", event);
		else {
			return "/errors/eventError.html";
		}
		return "admin/formUpdateEvent.html";
	}
	
	
	@GetMapping("/admin/updateOrchestra/{id}")
	public String updateOrchestra(@PathVariable("id") Long id,Model model) {
		List<Musician> musiciansToAdd = this.musicianService.findMusciansNotInEvent(id);
		model.addAttribute("musiciansToAdd", musiciansToAdd);
		Event event = this.eventService.findEventById(id);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/orchestraMemberToAdd.html";
		}
		else
			return "errors/eventError.html";
	}

	/*****************************ADD*******************************************/
	
	@GetMapping("/admin/addSoloist/{idSoloist}")
	public String addSoloist(@PathVariable("idSoloist") Long id, Model model) {
		model.addAttribute("musicians", this.musicianService.findAllMusicians());
		Event event = this.eventService.findEventById(id);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/soloistToAdd.html";
		}
		return "/errors/eventError.html";
		
	}
	
	@GetMapping("/admin/setSoloistToEvent/{idSoloist}/{idEvent}")
	public String setSoloistToEvent(@PathVariable("idSoloist") Long idSoloist, @PathVariable("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.saveSolistToEvent(idSoloist, idEvent);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/formUpdateEvent.html";
		}
		return "/errors/eventError.html";
		
	}
	
	
	@GetMapping("/admin/addOrchestraDirector/{idOrchestraDirector}")
	public String addOrchestraDirector(@PathVariable("idOrchestraDirector") Long id, Model model) {
		model.addAttribute("musicians", this.musicianService.findAllMusicians());
		Event event = this.eventService.findEventById(id);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/orchestraDirectorToAdd.html";
		}
		return "/errors/eventError.html";
		
	}
	
	@GetMapping("/admin/setOrchestraDirectorToEvent/{idOrchestraDirector}/{idEvent}")
	public String setOrchestraDirectorToEvent(@PathVariable("idOrchestraDirector") Long idOrchestraDirector, @PathVariable("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.saveOrchestraDirectorToEvent(idOrchestraDirector, idEvent);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/formUpdateEvent.html";
		}
		return "/errors/eventError.html";
		
	}
	
	
	
	@GetMapping("/admin/addOrchestraMemberToEvent/{idMusician}/{idEvent}")
	public String addOrchestraMemberToEvent(@PathVariable ("idMusician") Long idMusician, @PathVariable ("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.saveOrchestraMemberToEvent(idMusician,idEvent);
		if(event!=null) {
			List<Musician> musiciansToAdd = this.musicianService.findMusciansNotInEvent(idEvent);
			model.addAttribute("event",event);
			model.addAttribute("musiciansToAdd", musiciansToAdd);
			return "/admin/orchestraMemberToAdd.html";
		}
		else
			return "errors/eventError.html";
		
	}
	
	/*****************************REMOVE *******************************************/
	
	
	@GetMapping("/admin/removeSoloistFromEvent/{idSoloist}/{idEvent}")
	public String removeSoloistFromEvent(@PathVariable("idSoloist") Long idSoloist, @PathVariable("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.removeSoloistFromEvent(idSoloist, idEvent);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/formUpdateEvent.html";
		}
		return "/errors/eventError.html";
		
	}
	
	@GetMapping("/admin/removeOrchestraDirectorFromEvent/{idOrchestraDirector}/{idEvent}")
	public String removeOrchestraDirectorFromEvent(@PathVariable("idOrchestraDirector") Long idOrchestraDirector, @PathVariable("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.removeOrchestraDirectorFromEvent(idOrchestraDirector, idEvent);
		if(event!=null) {
			model.addAttribute("event",event);
			return "admin/formUpdateEvent.html";
		}
		return "/errors/eventError.html";
		
	}
	
	
	@GetMapping("/admin/removeOrchestraMemberFromEvent/{idMusician}/{idEvent}")
	public String removeOrchestraMemberFromEvent(@PathVariable ("idMusician") Long idMusician, @PathVariable ("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.removeOrchestraMemberFromEvent(idMusician,idEvent);
		if(event!=null) {
			List<Musician> musiciansToAdd = this.musicianService.findMusciansNotInEvent(idEvent);
			model.addAttribute("event",event);
			model.addAttribute("musiciansToAdd", musiciansToAdd);
			return "/admin/orchestraMemberToAdd.html";
		}
		else
			return "errors/eventError.html";
		
	}
	

	/****************************** REMOVE EVENT con tutti i dati ad esso collegati*****************************************/
	
	@GetMapping(value="/admin/removeEvent/{idEvent}")
	public String removeEvent(@PathVariable("idEvent") Long idEvent, Model model) {
		this.eventService.removeEvent(idEvent);
		return "/admin/indexEvent.html";
	}
	
	
	/****************************** REVIEWS *****************************************/



	@GetMapping(value="/registered/formAddReview/{idEvent}")
	public String addReview(@PathVariable("idEvent") Long idEvent, Model model) {
		model.addAttribute("event", this.eventService.findEventById(idEvent));
		model.addAttribute("review", new Review());
		return "/registered/formAddReview.html";
	}
	
	@PostMapping(value="/registered/addReviewToEvent/{idEvent}")
	public String addReviewToEventByUser(@Valid @ModelAttribute Review review, BindingResult bindingResult, @PathVariable("idEvent") Long idEvent, Model model) {
		if(this.userService.checkIfUserAlreadyReviewedAfilm(this.userService.getCurrentUser(), this.eventService.findEventById(idEvent)))
			return "errors/alreadyReviewed.html";
		
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.eventService.newReviewToEvent(review,idEvent);
			model.addAttribute("review", review);
			return "redirect:/event/" + idEvent;  
			
		}	
		model.addAttribute("event", this.eventService.findEventById(idEvent));
		return "/registered/formAddReview.html";
	}


	/****************************** UPDATE *****************************************/


	@GetMapping(value="/admin/posterChange/{idEvent}")
	public String posterChange(@PathVariable("idEvent") Long idEvent, Model model) {
		Event event = this.eventService.findEventById(idEvent);
		if(event!=null)
			model.addAttribute("event", event);
		else {
			return "/errors/eventError.html";
		}
		return "/admin/posterChange.html";
	}

	@PostMapping(value="/admin/updatePosterToEvent/{idEvent}")
	public String updatePosterToEvent(@PathVariable("idEvent") Long idEvent, 
			@Valid @ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResult, Model model) {

		this.fileValidator.validate(fileUpload, bindingResult);

		if (!bindingResult.hasErrors()) {
			try {
				model.addAttribute("event",this.eventService.updatePosterToEvent(idEvent,fileUpload.getImage())); 
				return "redirect:/admin/formUpdateEvent/" + idEvent;
			}
			catch (IOException e) {
				return "errors/fileUploadError.html";
			}
		} 
		else {
			return "errors/fileUploadError.html"; 
		}
	}
}
