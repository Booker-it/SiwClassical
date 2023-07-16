package it.uniroma3.siwClassical.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwClassical.model.Comment;
import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.model.User;
import it.uniroma3.siwClassical.repository.CommentRepository;
import it.uniroma3.siwClassical.repository.GroupRepository;

@Service
public class CommentService {

	@Autowired CommentRepository commentRepository;
	
	@Autowired GroupRepository groupRepository;
	
	@Autowired UserService userService;
	
	public boolean existsCommentByTextAndOrarioAndGroupAndAuthor(String text, LocalDateTime date, Group group, User author) {
		return this.commentRepository.existsByTextAndOrarioAndGroupCommentAndAuthor(text, date, group, author);
	}
	
	public Iterable<Comment> findAllComments(){
		return this.commentRepository.findAll();
	}
}
