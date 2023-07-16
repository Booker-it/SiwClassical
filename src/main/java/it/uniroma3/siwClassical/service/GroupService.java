package it.uniroma3.siwClassical.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwClassical.model.Comment;
import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.model.User;
import it.uniroma3.siwClassical.repository.CommentRepository;
import it.uniroma3.siwClassical.repository.EventRepository;
import it.uniroma3.siwClassical.repository.GroupRepository;
import jakarta.transaction.Transactional;

@Service
public class GroupService {

	@Autowired GroupRepository groupRepository;

	@Autowired UserService userService;
	
	@Autowired EventService eventService;
	
	@Autowired EventRepository eventRepository;
	
	@Autowired CommentRepository commentRepository;

	public Iterable<Group> findAllGroups() {
		return this.groupRepository.findAll();
	}

	public Iterable<Group> findAllGroupsOfEvent(Long idEvent) {
		return this.groupRepository.findAllGroupsOfEvent(idEvent);
	}
	
	public Group findGroupById(Long idGroup) {
		return this.groupRepository.findById(idGroup).orElse(null);
	}
	
	public boolean existsGroupByNameAndGroupCreator(String name, User autore) {
		return this.groupRepository.existsByNameAndGroupCreator(name, autore);
	}
	
	@Transactional
	public Group newGroupToEvent(Group group, Long idEvent) {
		Event event = this.eventRepository.findById(idEvent).orElse(null);
		
		group.setGroupCreator(this.userService.getCurrentUser());
		
		
		group.setEvent(event);
		
		event.getGroups().add(group);
		
		this.userService.getCurrentUser().getCreatedGroup().add(group);

		this.groupRepository.save(group);
		this.eventRepository.save(event);
		
		return group;
	}
	
	@Transactional
	public Group newCommentToGroup(Comment comment, Long idGroup) {
		Group group = this.groupRepository.findById(idGroup).orElse(null);
		
		comment.setAuthor(this.userService.getCurrentUser());
		comment.setOrario(LocalDateTime.now());
		
		comment.setGroupComment(group);
		group.getComments().add(comment);
		
		this.userService.getCurrentUser().getComments().add(comment);
		
		this.commentRepository.save(comment);
		this.groupRepository.save(group);
		return group;
	}

	public Iterable<Comment> commentsOfGroupOrderByDate(Long idGroup){
		return this.commentRepository.commentsOfGroupOrderByDate(idGroup);
	}
	
}
