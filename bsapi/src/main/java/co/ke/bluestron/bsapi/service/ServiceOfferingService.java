package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.ServiceOfferingDTO;
import co.ke.bluestron.bsapi.entity.ServiceOffering;
import co.ke.bluestron.bsapi.repository.ServiceOfferingRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceOfferingService {
    private final ServiceOfferingRepository repo;
    
    public ServiceOfferingService(ServiceOfferingRepository repo) {
        this.repo = repo;
    }
    
    public List<ServiceOffering> list(String status) {
        return repo.findByFilters(status);
    }
    
    public ServiceOffering getBySlug(String slug) {
        return repo.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Service offering not found"));
    }
    
    @Transactional
    public ServiceOffering create(ServiceOfferingDTO dto, String username) {
        ServiceOffering s = new ServiceOffering();
        s.setSlug(dto.slug());
        s.setName(dto.name());
        s.setDescription(dto.description());
        s.setIcon(dto.icon());
        s.setStatus(dto.status() != null ? dto.status() : "active");
        s.setCreatedBy(username);
        s.setUpdatedBy(username);
        return repo.save(s);
    }
}