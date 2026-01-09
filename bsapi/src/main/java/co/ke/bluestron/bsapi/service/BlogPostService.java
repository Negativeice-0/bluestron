package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.BlogPostDTO;
import co.ke.bluestron.bsapi.entity.BlogPost;
import co.ke.bluestron.bsapi.repository.BlogPostRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BlogPostService {
    private final BlogPostRepository repo;
    
    public BlogPostService(BlogPostRepository repo) {
        this.repo = repo;
    }
    
    public List<BlogPost> list(String status) {
        return repo.findByFilters(status);
    }
    
    public BlogPost getBySlug(String slug) {
        return repo.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Blog post not found"));
    }
    
    @Transactional
    public BlogPost create(BlogPostDTO dto, String username) {
        BlogPost b = new BlogPost();
        b.setSlug(dto.slug());
        b.setTitle(dto.title());
        b.setExcerpt(dto.excerpt());
        b.setContent(dto.content());
        b.setFeaturedImage(dto.featuredImage());
        b.setAuthor(dto.author());
        b.setPublishedDate(dto.publishedDate());
        b.setStatus(dto.status() != null ? dto.status() : "draft");
        b.setCreatedBy(username);
        b.setUpdatedBy(username);
        return repo.save(b);
    }
}