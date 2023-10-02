package ru.practicum.exploreWithMe.commonFiles.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.commonFiles.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c " +
		"FROM Comment AS c " +
		"WHERE :userId IS NULL OR c.author.id = :userId")
	List<Comment> getUserComments(@Param("userId") Long userId, Pageable pageable);

	@Query("SELECT c " +
		"FROM Comment AS c " +
		"WHERE :eventId IS NULL OR c.event.id = :eventId")
	List<Comment> getEventComments(@Param("eventId") Long eventId);

	@Query("SELECT c " +
		"FROM Comment AS c " +
		"WHERE :events IS NULL OR c.event.id IN (:events)")
	List<Comment> getEventsComments(@Param("events") List<Long> events);
}
