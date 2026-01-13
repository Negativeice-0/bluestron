// File: src/main/java/com/bluestron/repository/CourseRepository.java
package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find a course by its unique slug
    Optional<Course> findBySlug(String slug);

    // Find all courses that are marked as featured
    List<Course> findByIsFeaturedTrue();

    // Optionally: find all courses within a given category
    List<Course> findByCategoryId(Long categoryId);
}
