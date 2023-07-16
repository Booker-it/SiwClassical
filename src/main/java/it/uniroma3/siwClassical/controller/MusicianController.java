package it.uniroma3.siwClassical.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siwClassical.controller.utility.FileUploadUtil;
import it.uniroma3.siwClassical.controller.validator.FileValidator;
import it.uniroma3.siwClassical.controller.validator.MusicianValidator;
import it.uniroma3.siwClassical.model.Musician;
import it.uniroma3.siwClassical.service.MusicianService;
import jakarta.validation.Valid;

@Controller
public class MusicianController {

	@Autowired MusicianService musicianService;

	@Autowired MusicianValidator musicianValidator;

	@Autowired FileValidator fileValidator;

	@GetMapping("/musician")
	public String getMusicians(Model model) {
		model.addAttribute("musicians", this.musicianService.findAllMusicians());
		return "/musicians.html";
	}

	@GetMapping("/musician/{idMusician}")
	public String getMusician(@PathVariable("idMusician") Long idMusician,Model model) {
		Musician musician = this.musicianService.findMusicianById(idMusician);
		if(musician!=null) {	
			model.addAttribute("musician", musician);
			return "/musician.html";
		}
		else 
			return "errors/musicianError";
	}

	@GetMapping(value="/admin/indexMusician")
	public String indexMusician(Model model) {
		return "/admin/indexMusician.html";
	}


	@GetMapping("/admin/manageMusicians")
	public String manageMusicians(Model model) {
		model.addAttribute("musicians", this.musicianService.findAllMusicians());
		return "/admin/manageMusicians.html";

	}


	@GetMapping("/admin/formNewMusician")
	public String newMusician(Model model) {
		model.addAttribute("musician", new Musician());
		return "/admin/formNewMusician.html";

	}

	@PostMapping(value = "/admin/musician")
	public String newMusician(@Valid @ModelAttribute("musician") Musician musician, BindingResult bindingResultMusician,
			@Valid @ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResultFile, Model model) {

		this.musicianValidator.validate(musician, bindingResultMusician);
		this.fileValidator.validate(fileUpload, bindingResultFile);

		if(!bindingResultFile.hasErrors() && !bindingResultMusician.hasErrors()) {
			try {
				model.addAttribute("musician", this.musicianService.createNewMusician(musician,fileUpload.getImage()));
				return "musician.html";

			}
			catch(IOException e) {
				model.addAttribute("messaggioErrore", "Errore nell'upload");
				return "admin/formNewArtist.html";
			}
		}
		else
			return "/admin/formNewMusician.html";
	}

	/**************************** Eliminazione Musicista Admin ************************************************/

	@GetMapping(value="/admin/removeMusician/{idMusician}")
	public String removeMovie(@PathVariable("idMusician") Long idMusician, Model model) {
		this.musicianService.removeMusician(idMusician);
		model.addAttribute("musicians", this.musicianService.findAllMusicians());
		return "/admin/manageMusicians.html";
	}


	/**************************** UPDATE ************************************************/

	@GetMapping("/admin/photoChange/{idMusician}")
	public String photoChange(@PathVariable("idMusician") Long idMusician,Model model) {
		Musician musician = this.musicianService.findMusicianById(idMusician);
		if(musician!=null)
			model.addAttribute("musician",musician);
		else {
			return "/errors/musicianError";
		}
		return "/admin/photoChange";
	}

	@PostMapping("/admin/updatePhotoToMusician/{idMusician}")
	public String updatePhotoToMusician(@PathVariable ("idMusician") Long idMusician,
			@Valid @ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResult, Model model) {

		this.fileValidator.validate(fileUpload, bindingResult);

		if(!bindingResult.hasErrors()) {
			try {
				this.musicianService.updatePhotoToMusician(idMusician,fileUpload.getImage());
				model.addAttribute("musicians",this.musicianService.findAllMusicians());
				return "admin/manageMusicians";
			}
			catch(IOException e) {
				model.addAttribute("messaggioErrore", "Upload non riuscito!");
				model.addAttribute("musicians",this.musicianService.findAllMusicians());
				return "admin/manageMusicians";
			}
		}
		else {
			model.addAttribute("musicians",this.musicianService.findAllMusicians());
			return "admin/manageMusicians";
		}

	}


	@GetMapping("/admin/dateChange/{idMusician}")
	public String dateChange(@PathVariable("idMusician") Long idMusician,Model model) {
		Musician musician = this.musicianService.findMusicianById(idMusician);
		if(musician!=null)
			model.addAttribute("musician",musician);
		else {
			return "/errors/musicianError";
		}
		return "/admin/dateChange";
	}

	
	@PostMapping("/admin/updateDateOfDeath/{idMusician}")
	public String updateDateOfDeath(@PathVariable("idMusician") Long idMusician, @RequestParam LocalDate dateOfDeath,Model model) {
		
		this.musicianService.changeDateOfDeath(idMusician, dateOfDeath);
		model.addAttribute("musicians",this.musicianService.findAllMusicians());
		return "admin/manageMusicians";
	}
	
	@PostMapping("/admin/removeDateOfDeath/{idMusician}")
	public String removeDateOfDeath(@PathVariable("idMusician") Long idMusician,Model model) {
		
		this.musicianService.removeDateOfDeath(idMusician);
		model.addAttribute("musicians",this.musicianService.findAllMusicians());
		return "admin/manageMusicians";
	}

}
