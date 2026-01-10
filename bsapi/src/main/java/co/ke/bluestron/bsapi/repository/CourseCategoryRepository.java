package co.ke.bluestron.bsapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.bluestron.bsapi.entity.CourseCategory;

/**
 * JPA repository with convenience lookups.
 */
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    Optional<CourseCategory> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
