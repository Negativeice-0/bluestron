package co.ke.bluestron.bsapi.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.Testimonial;
import co.ke.bluestron.bsapi.repository.TestimonialRepository;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {
    private final TestimonialRepository repository;
    
    public TestimonialController(TestimonialRepository repo) {
        this.repository = repo;
    }
    
    @GetMapping
    public List<Testimonial> getFeatured() {
        return repository.findByFeaturedTrueOrderByCreatedAtDesc();
    }
}
