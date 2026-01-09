package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.TrainerDTO;
import co.ke.bluestron.bsapi.entity.Trainer;
import co.ke.bluestron.bsapi.repository.TrainerRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TrainerService {
    private final TrainerRepository repo;
    
    public TrainerService(TrainerRepository repo) {
        this.repo = repo;
    }
    
    public List<Trainer> list(String status) {
        return repo.findByFilters(status);
    }
    
    public Trainer getBySlug(String slug) {
        return repo.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));
    }
    
    @Transactional
    public Trainer create(TrainerDTO dto, String username) {
        Trainer t = new Trainer();
        t.setSlug(dto.slug());
        t.setFullName(dto.fullName());
        t.setTitle(dto.title());
        t.setBio(dto.bio());
        t.setPhotoUrl(dto.photoUrl());
        t.setTwitter(dto.twitter());
        t.setLinkedin(dto.linkedin());
        t.setGithub(dto.github());
        t.setStatus(dto.status() != null ? dto.status() : "active");
        t.setCreatedBy(username);
        t.setUpdatedBy(username);
        return repo.save(t);
    }
}