package it.uniroma3.siwClassical.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwClassical.model.Comment;
import it.uniroma3.siwClassical.service.CommentService;

@Component
public class CommentValidator implements Validator{
	
	@Autowired CommentService commentService;
	
	
	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Comment comment = (Comment)target;
		if(comment!=null && this.commentService.existsCommentByTextAndOrarioAndGroupAndAuthor(comment.getText(), comment.getOrario(), comment.getGroupComment(), comment.getAuthor()))
			errors.reject("comment.duplicate");
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Comment.class.equals(clazz);
	}

}
