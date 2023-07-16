package it.uniroma3.siwClassical.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>{
	
	public boolean existsByNameAndLocationAndCityAndDateEvent(String name, String location, String City, LocalDateTime dateEvent);
	
	@Query(value="select * from Event e Order by date_event",nativeQuery=true)
	public List<Event> findAllOrderByDate();

}
