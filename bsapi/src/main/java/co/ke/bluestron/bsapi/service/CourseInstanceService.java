package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ke.bluestron.bsapi.dto.CourseInstanceDTO;
import co.ke.bluestron.bsapi.entity.CourseInstance;
import co.ke.bluestron.bsapi.repository.CourseInstanceRepository;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CourseInstanceService {
    private final CourseInstanceRepository repo;
    private final CourseRepository courseRepo;
    private final VenueRepository venueRepo;
    
    public CourseInstanceService(CourseInstanceRepository repo, 
                                 CourseRepository courseRepo,
                                 VenueRepository venueRepo) {
        this.repo = repo;
        this.courseRepo = courseRepo;
        this.venueRepo = venueRepo;
    }
    
    public List<CourseInstance> list(Long courseId, String status) {
        return repo.findByFilters(courseId, status);
    }
    
    @Transactional
    public CourseInstance create(CourseInstanceDTO dto, String username) {
        CourseInstance instance = new CourseInstance();
        
        // Set course
        instance.setCourse(courseRepo.findById(dto.courseId())
            .orElseThrow(() -> new EntityNotFoundException("Course not found")));
        
        // Set venue if in_person mode
        if ("in_person".equals(dto.mode()) && dto.venueId() != null) {
            instance.setVenue(venueRepo.findById(dto.venueId())
                .orElseThrow(() -> new EntityNotFoundException("Venue not found")));
        }
        
        // Set other fields
        instance.setMode(dto.mode());
        instance.setStartDate(dto.startDate());
        instance.setEndDate(dto.endDate());
        instance.setCapacity(dto.capacity());
        instance.setStatus(dto.status() != null ? dto.status() : "open");
        instance.setCreatedBy(username);
        instance.setUpdatedBy(username);
        
        return repo.save(instance);
    }
}