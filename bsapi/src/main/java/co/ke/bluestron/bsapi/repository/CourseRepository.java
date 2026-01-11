package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByFeaturedTrue();
    List<Course> findByCategoryId(Integer categoryId);
    Course findBySlug(String slug);
}