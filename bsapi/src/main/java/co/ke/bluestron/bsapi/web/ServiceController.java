package co.ke.bluestron.bsapi.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.Service;
import co.ke.bluestron.bsapi.repository.ServiceRepository;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceRepository repository;
    
    public ServiceController(ServiceRepository repo) {
        this.repository = repo;
    }
    
    @GetMapping
    public List<Service> getAll() {
        return repository.findAll();  // Return all professional services
    }
    
    @GetMapping("/category/{slug}")
    public List<Service> getByCategory(@PathVariable String slug) {
        return repository.findByCategory_Slug(slug);  // Services filtered by category
    }
}
