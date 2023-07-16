package it.uniroma3.siwClassical.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Credentials;

@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);
	
	public boolean existsByUsername(String username);

}