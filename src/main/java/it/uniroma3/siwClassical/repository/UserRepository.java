package it.uniroma3.siwClassical.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}