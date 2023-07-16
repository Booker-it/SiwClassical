package it.uniroma3.siwClassical.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwClassical.model.Comment;
import it.uniroma3.siwClassical.model.Group;
import it.uniroma3.siwClassical.model.User;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
	
	public boolean existsByTextAndOrarioAndGroupCommentAndAuthor(String text, LocalDateTime date, Group group, User author);


	@Query(value="select * from Comment c where c.group_comment_id = :idGroup Order by c.orario", nativeQuery=true)
	public List<Comment> commentsOfGroupOrderByDate(@Param("idGroup") Long id);
	
}
