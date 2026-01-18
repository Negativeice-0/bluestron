package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.Testimonial;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    // Find featured testimonials ordered by creation date descending
    List<Testimonial> findByFeaturedTrueOrderByCreatedAtDesc();
}

