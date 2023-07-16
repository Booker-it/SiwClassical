package it.uniroma3.siwClassical.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Event;
import it.uniroma3.siwClassical.model.Review;
import it.uniroma3.siwClassical.model.User;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long>{

	
	
	public boolean existsByTitleAndAuthorAndTextAndEventReviewed(String title, User author, String text, Event eventReviewed);
}
