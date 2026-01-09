package co.ke.bluestron.bsapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.ServiceEnquiryDTO;
import co.ke.bluestron.bsapi.entity.ServiceEnquiry;
import co.ke.bluestron.bsapi.repository.ServiceEnquiryRepository;
import co.ke.bluestron.bsapi.repository.ServiceOfferingRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceEnquiryService {
    private final ServiceEnquiryRepository repo;
    private final ServiceOfferingRepository serviceRepo;
    
    public ServiceEnquiryService(ServiceEnquiryRepository repo, 
                                 ServiceOfferingRepository serviceRepo) {
        this.repo = repo;
        this.serviceRepo = serviceRepo;
    }
    
    @Transactional
    public ServiceEnquiry submit(ServiceEnquiryDTO dto, String username) {
        ServiceEnquiry se = new ServiceEnquiry();
        
        // Set service if provided
        if (dto.serviceId() != null) {
            se.setService(serviceRepo.findById(dto.serviceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found")));
        }
        
        se.setFullName(dto.fullName());
        se.setEmail(dto.email());
        se.setPhone(dto.phone());
        se.setCompany(dto.company());
        se.setMessage(dto.message());
        se.setStatus("new");
        se.setCreatedBy(username);
        se.setUpdatedBy(username);
        
        return repo.save(se);
    }
}