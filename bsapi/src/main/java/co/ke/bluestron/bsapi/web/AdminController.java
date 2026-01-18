package co.ke.bluestron.bsapi.web;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.BlogPost;
import co.ke.bluestron.bsapi.model.Course;
import co.ke.bluestron.bsapi.model.Registration;
import co.ke.bluestron.bsapi.repository.BlogPostRepository;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.RegistrationRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final CourseRepository courseRepo;
    private final BlogPostRepository blogRepo;
    private final RegistrationRepository registrationRepo;

    public AdminController(CourseRepository courseRepo,
                           BlogPostRepository blogRepo,
                           RegistrationRepository registrationRepo) {
        this.courseRepo = courseRepo;
        this.blogRepo = blogRepo;
        this.registrationRepo = registrationRepo;
    }

    @GetMapping("/dashboard/stats")
    public Map<String, Object> getDashboardStats() {
        long totalCourses = courseRepo.count();
        long totalRegistrations = registrationRepo.count();
        long totalPosts = blogRepo.count();

        return Map.of(
            "totalCourses", totalCourses,
            "totalRegistrations", totalRegistrations,
            "totalPosts", totalPosts
        );
    }

    @GetMapping("/registrations")
    public List<Registration> getAllRegistrations() {
        return registrationRepo.findAll();
    }

    @PutMapping("/registrations/{id}/status")
    public Registration updateRegistrationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate
    ) {
        Registration reg = registrationRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.setStatus(statusUpdate.get("status"));
        return registrationRepo.save(reg);
    }

    @PostMapping("/blog")
    public BlogPost createBlogPost(@RequestBody BlogPost post) {
        return blogRepo.save(post);
    }

    @PutMapping("/courses/{id}/feature")
    public Course toggleFeatured(@PathVariable Long id) {
        Course course = courseRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setIsFeatured(!course.getIsFeatured());
        return courseRepo.save(course);
    }
}
