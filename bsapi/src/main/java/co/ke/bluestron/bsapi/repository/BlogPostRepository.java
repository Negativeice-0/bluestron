package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Optional<BlogPost> findBySlug(String slug);
    @Query("SELECT b FROM BlogPost b WHERE (:status IS NULL OR b.status = :status)")
    List<BlogPost> findByFilters(String status);
}