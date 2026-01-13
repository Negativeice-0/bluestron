package co.ke.bluestron.bsapi.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.Category;
import co.ke.bluestron.bsapi.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repo) {
        this.repository = repo;  // Inject the repository – the gatekeeper of category data
    }

    @GetMapping
    public List<Category> getAll() {
        return repository.findAll();  // Return the entire catalog of categories
    }

    @GetMapping("/{slug}")
    public Category getBySlug(@PathVariable String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}