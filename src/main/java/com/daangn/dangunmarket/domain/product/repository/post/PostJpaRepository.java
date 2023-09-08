package com.daangn.dangunmarket.domain.product.repository.post;

import com.daangn.dangunmarket.domain.product.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.isDeleted = false and p.id = :id")
    Optional<Post> findById(@Param(value = "id") Long id);

}
