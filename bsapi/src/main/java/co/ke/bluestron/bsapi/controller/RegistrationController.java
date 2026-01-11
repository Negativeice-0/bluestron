package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.entity.Registration;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.RegistrationRepository;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
    
    @PostMapping
    public Registration createRegistration(@RequestBody Registration registration) {
        // Ensure course exists
        if (registration.getCourse() != null && registration.getCourse().getId() != null) {
            courseRepository.findById(registration.getCourse().getId())
                .ifPresent(registration::setCourse);
        }
        return registrationRepository.save(registration);
    }
}