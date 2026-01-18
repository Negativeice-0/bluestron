package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    // Find all published posts ordered by publishedAt descending
    List<BlogPost> findByPublishedTrueOrderByPublishedAtDesc();

    // Find a published post by slug
    Optional<BlogPost> findBySlugAndPublishedTrue(String slug);
}

