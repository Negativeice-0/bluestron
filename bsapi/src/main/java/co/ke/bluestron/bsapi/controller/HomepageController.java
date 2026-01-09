package co.ke.bluestron.bsapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.repository.BlogPostRepository;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.FeaturedContentRepository;
import co.ke.bluestron.bsapi.repository.ServiceOfferingRepository;
import co.ke.bluestron.bsapi.repository.TestimonialRepository;
import co.ke.bluestron.bsapi.repository.TrainerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Homepage")
@RestController @RequestMapping("/api/homepage")
public class HomepageController {
    private final FeaturedContentRepository featuredRepo;
    private final CourseRepository courseRepo;
    private final BlogPostRepository blogRepo;
    private final ServiceOfferingRepository serviceRepo;
    private final TrainerRepository trainerRepo;
    private final TestimonialRepository testimonialRepo;

    public HomepageController(FeaturedContentRepository f, CourseRepository c, BlogPostRepository b,
                              ServiceOfferingRepository s, TrainerRepository t, TestimonialRepository tt) {
        this.featuredRepo = f; this.courseRepo = c; this.blogRepo = b; this.serviceRepo = s;
        this.trainerRepo = t; this.testimonialRepo = tt;
    }

    @Operation(summary = "Homepage composite: featured items, trainers, testimonials")
    @GetMapping
    public ResponseEntity<Map<String, Object>> composite() {
        Map<String, Object> payload = new HashMap<>();
        
        // Get featured content for homepage section, active status, ordered by display order
        var featured = featuredRepo.findByFilters("homepage", "active");
        payload.put("featured", featured.stream().map(fc -> {
            Map<String, Object> item = new HashMap<>();
            item.put("kind", fc.getContentType());  // Changed from getKind() to getContentType()
            item.put("position", fc.getDisplayOrder());  // Changed from getPosition()
            
            // Fetch referenced content based on type
            switch (fc.getContentType()) {
                case "course" -> item.put("data", courseRepo.findById(fc.getContentId()).orElse(null));
                case "blog" -> item.put("data", blogRepo.findById(fc.getContentId()).orElse(null));
                case "service" -> item.put("data", serviceRepo.findById(fc.getContentId()).orElse(null));
                default -> item.put("data", null);
            }
            
            // Add custom title/description if provided
            if (fc.getCustomTitle() != null) {
                item.put("customTitle", fc.getCustomTitle());
            }
            if (fc.getCustomDescription() != null) {
                item.put("customDescription", fc.getCustomDescription());
            }
            
            return item;
        }).toList());
        
        // Get active trainers
        payload.put("trainers", trainerRepo.findByFilters("active"));
        
        // Get active testimonials
        payload.put("testimonials", testimonialRepo.findByFilters("active"));
        
        return ResponseEntity.ok(payload);
    }
}