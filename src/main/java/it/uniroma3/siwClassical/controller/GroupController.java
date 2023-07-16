package it.uniroma3.siwClassical.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siwClassical.controller.validator.CommentValidator;
import it.uniroma3.siwClassical.controller.validator.GroupValidator;
import it.uniroma3.siwClassical.model.Comment;
import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.service.CommentService;
import it.uniroma3.siwClassical.service.EventService;
import it.uniroma3.siwClassical.service.GroupService;
import jakarta.validation.Valid;

@Controller
public class GroupController {

	@Autowired GroupService groupService;
	@Autowired GroupValidator groupValidator;
	@Autowired EventService eventService;
	@Autowired CommentValidator commentValidator;
	@Autowired CommentService commentService;

	@GetMapping(value="/registered/formAddGroup/{idEvent}")
	public String addGroup(@PathVariable("idEvent") Long idEvent, Model model) {
		model.addAttribute("event", this.eventService.findEventById(idEvent));
		model.addAttribute("group", new Group());
		return "/registered/formAddGroup.html";
	}
	
	@PostMapping(value="/registered/addGroupToEvent/{idEvent}")
	public String addGroupToEventByUser(@Valid @ModelAttribute Group group, BindingResult bindingResult, @PathVariable("idEvent") Long idEvent, Model model) {
		
		this.groupValidator.validate(group, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.groupService.newGroupToEvent(group,idEvent);
			model.addAttribute("group", group);
			return "redirect:/event/" + idEvent;  
			
		}	
		model.addAttribute("event", this.eventService.findEventById(idEvent));
		return "/registered/formAddGroup.html";
	}
	
	
	@GetMapping("/registered/indexGroup/{idEvent}")
	public String getGroups(@PathVariable("idEvent") Long idEvent,Model model) {
		model.addAttribute("event", this.eventService.findEventById(idEvent));
		model.addAttribute("groups", this.groupService.findAllGroupsOfEvent(idEvent));
		return "/registered/indexGroup.html";
	}
	
//	@GetMapping("/registered/group/{idGroup}")
//	public String getGroup(@PathVariable("idGroup") Long id, Model model) {
//			model.addAttribute("group",this.groupService.findGroupById(id));
//			model.addAttribute("comment",new Comment());
//			return "/registered/group.html";
//	}
	
	
	@GetMapping("/registered/group/{idGroup}")
	public String getGroup(@PathVariable("idGroup") Long idEvent, Model model) {
			Iterable<Comment> comments = this.groupService.commentsOfGroupOrderByDate(idEvent);
			
			model.addAttribute("group",this.groupService.findGroupById(idEvent));
			model.addAttribute("comment",new Comment());
			model.addAttribute("commentGroup", comments);
			return "/registered/group.html";
	}
	
	
	@PostMapping(value="/registered/addCommentToEvent/{idGroup}")
	public String addCommentToGroupByUser(@Valid @ModelAttribute Comment comment, BindingResult bindingResult, @PathVariable("idGroup") Long idGroup, Model model) {
		
		this.commentValidator.validate(comment, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.groupService.newCommentToGroup(comment, idGroup);
			
			model.addAttribute("comment",comment);
			return "redirect:/registered/group/" + idGroup;  
		}	
		return "index";
	}
	
}
