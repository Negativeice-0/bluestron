package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.TestimonialDTO;
import co.ke.bluestron.bsapi.entity.Testimonial;
import co.ke.bluestron.bsapi.repository.TestimonialRepository;

@Service
public class TestimonialService {
    private final TestimonialRepository repo;
    
    public TestimonialService(TestimonialRepository repo) {
        this.repo = repo;
    }
    
    public List<Testimonial> list(String status) {
        return repo.findByFilters(status);
    }
    
    @Transactional
    public Testimonial create(TestimonialDTO dto, String username) {
        Testimonial t = new Testimonial();
        t.setAuthorName(dto.authorName());
        t.setAuthorRole(dto.authorRole());
        t.setAuthorCompany(dto.authorCompany());
        t.setContent(dto.content());
        t.setPhotoUrl(dto.photoUrl());
        t.setRating(dto.rating());
        t.setStatus(dto.status() != null ? dto.status() : "active");
        t.setCreatedBy(username);
        t.setUpdatedBy(username);
        return repo.save(t);
    }
}