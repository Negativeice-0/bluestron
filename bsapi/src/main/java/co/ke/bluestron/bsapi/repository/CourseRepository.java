package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findBySlug(String slug);

    @Query("SELECT c FROM Course c WHERE (:categoryId IS NULL OR c.category.id = :categoryId) AND (:status IS NULL OR c.status = :status)")
    List<Course> findByFilters(Long categoryId, String status);
}
