package ru.practicum.exploreWithMe.commonFiles.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u " +
            "FROM User AS u " +
            "WHERE (:users IS NULL OR u.id IN :users)")
    List<User> getUsersByIds(@Param("users") Set<Long> users, Pageable pageable);
}
