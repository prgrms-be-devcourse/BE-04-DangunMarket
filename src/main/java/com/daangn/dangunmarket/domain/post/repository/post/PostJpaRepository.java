package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.post.model.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {


    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.localPreference lp WHERE p.isDeleted = false AND p.id = :id")
    Optional<Post> findById(@Param(value = "id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.id = :id")
    Optional<Post> findByIdForUpdate(@Param(value = "id") Long id);

}
