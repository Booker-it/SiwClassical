package it.uniroma3.siwClassical.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Review;
import it.uniroma3.siwClassical.service.ReviewService;
import it.uniroma3.siwClassical.service.UserService;


@Controller
public class ReviewController {

	
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserService userService;
	
	
	/******************* ADMIN ********************/
	@GetMapping(value="/admin/removeReview/{reviewId}")
	public String removeReviewFromEventByAdmin(@PathVariable("reviewId") Long reviewId){
		Review review = this.reviewService.findReviewById(reviewId);
		
		if(review == null)
			return "/errors/reviewError.html";
		
		Event event = review.getEventReviewed();
		event.getReviews().remove(review);
		this.reviewService.removeReviewByAdmin(reviewId);
		
		
		return "redirect:/admin/formUpdateEvent/"+ event.getId();
	}
	
	/******************* User ********************/
	@GetMapping(value="/register/removeReview/{reviewId}")
	public String removeReviewFromEventByCreator(@PathVariable("reviewId") Long reviewId){
		Review review = this.reviewService.findReviewById(reviewId);
		
		if(review == null || !review.getAuthor().equals(this.userService.getCurrentUser()))
			return "/errors/reviewError.html";
		else {
			Event event = review.getEventReviewed();
			event.getReviews().remove(review);
			this.reviewService.removeReviewByCreator(reviewId);
			
			return "redirect:/event/" + event.getId();
		}
	}
	
}
