package it.uniroma3.siwClassical.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Image;
@Repository
public interface ImageRepository extends CrudRepository<Image, Long>{
	
	Optional<Image> findByName(String name);


}
