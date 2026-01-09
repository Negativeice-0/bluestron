package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.FeaturedContentDTO;
import co.ke.bluestron.bsapi.entity.FeaturedContent;
import co.ke.bluestron.bsapi.repository.FeaturedContentRepository;

@Service
public class FeaturedContentService {
    private final FeaturedContentRepository repo;
    
    public FeaturedContentService(FeaturedContentRepository repo) {
        this.repo = repo;
    }
    
    public List<FeaturedContent> list(String section, String status) {
        return repo.findByFilters(section, status);
    }
    
    @Transactional
    public FeaturedContent create(FeaturedContentDTO dto, String username) {
        FeaturedContent fc = new FeaturedContent();
        fc.setSection(dto.section());
        fc.setContentType(dto.contentType());
        fc.setContentId(dto.contentId());
        fc.setCustomTitle(dto.customTitle());
        fc.setCustomDescription(dto.customDescription());
        fc.setDisplayOrder(dto.displayOrder());
        fc.setStatus(dto.status() != null ? dto.status() : "active");
        fc.setCreatedBy(username);
        fc.setUpdatedBy(username);
        return repo.save(fc);
    }
}