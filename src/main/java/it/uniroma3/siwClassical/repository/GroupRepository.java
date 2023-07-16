package it.uniroma3.siwClassical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.model.User;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

	
	public boolean existsByNameAndGroupCreator(String name, User autore);
	
	
	@Query(value="select * from Groups g where g.event_id = :idEvent", nativeQuery=true)
	public List<Group> findAllGroupsOfEvent(@Param("idEvent") Long id);
	
	
}
