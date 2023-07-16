package it.uniroma3.siwClassical.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Review;
import it.uniroma3.siwClassical.model.User;
import it.uniroma3.siwClassical.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	@Autowired ReviewRepository reviewRepository;
	
	public Review findReviewById(Long idReview) {
		return this.reviewRepository.findById(idReview).orElse(null);
	}
	
	
	@Transactional
	public void removeReviewByAdmin(Long idReview) {
		this.reviewRepository.delete(this.findReviewById(idReview));
		
	}
	
	@Transactional
	public void removeReviewByCreator(Long idReview) {
		this.reviewRepository.delete(this.findReviewById(idReview));
		
	}
	
	
	public boolean existsReviewByTitleAndAuthorAndTextAndEventReviewed(String title, User author, String text, Event eventReviewed) {
		return this.reviewRepository.existsByTitleAndAuthorAndTextAndEventReviewed(title, author, text, eventReviewed);
	}
}
