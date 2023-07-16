package it.uniroma3.siwClassical.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwClassical.model.Review;
import it.uniroma3.siwClassical.service.ReviewService;

@Component
public class ReviewValidator implements Validator {
	
	@Autowired ReviewService reviewService;

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Review review = (Review)target;
		if(review.getTitle() != null && review.getText() != null && review.getAuthor() != null
				&& review.getRating() != null 
				&& this.reviewService.existsReviewByTitleAndAuthorAndTextAndEventReviewed(review.getTitle(), review.getAuthor(), review.getText(), review.getEventReviewed()))
			errors.reject("review.duplicate");

		if(review.getRating()<1)
			errors.reject("Min.review.rating");
		if(review.getRating()>5)
			errors.reject("Max.review.rating");

	}


	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Review.class.equals(clazz);
	}

}
