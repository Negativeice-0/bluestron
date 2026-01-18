package co.ke.bluestron.bsapi.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.BlogPost;
import co.ke.bluestron.bsapi.repository.BlogPostRepository;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogPostRepository repository;
    
    public BlogController(BlogPostRepository repo) {
        this.repository = repo;
    }
    
    @GetMapping
    public List<BlogPost> getPublished() {
        // Only show published posts – the finished articles on display
        return repository.findByPublishedTrueOrderByPublishedAtDesc();
    }
    
    @GetMapping("/{slug}")
    public BlogPost getBySlug(@PathVariable String slug) {
        return repository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
    }
}
