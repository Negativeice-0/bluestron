// File: src/main/java/com/bluestron/repository/CourseRepository.java
package co.ke.bluestron.bsapi.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find a course by its unique slug
    Optional<Course> findBySlug(String slug);
    
    List<Course> findByTitleContainingIgnoreCase(String keyword);

    List<Course> findByPriceBetween(BigDecimal min, BigDecimal max);

    // Find all courses that are marked as featured
    List<Course> findByIsFeaturedTrue();

    // Optionally: find all courses within a given category
    // Correct way: navigate into the Category relation
    // model = category, so Category_Id not categoryid
    //jpa uses category and spring uses id
    List<Course> findByCategory_Id(Long categoryId);
}
