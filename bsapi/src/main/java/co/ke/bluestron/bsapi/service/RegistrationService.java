package co.ke.bluestron.bsapi.service;

import org.springframework.stereotype.Service;

import co.ke.bluestron.bsapi.dto.RegistrationDTO;
import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.entity.CourseInstance;
import co.ke.bluestron.bsapi.entity.Registration;
import co.ke.bluestron.bsapi.repository.CourseInstanceRepository;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import co.ke.bluestron.bsapi.repository.RegistrationRepository;
import jakarta.transaction.Transactional;

@Service
public class RegistrationService {
    private final CourseRepository courseRepo;
    private final CourseInstanceRepository instanceRepo;
    private final RegistrationRepository regRepo;

    public RegistrationService(CourseRepository c, CourseInstanceRepository i, RegistrationRepository r) {
        this.courseRepo = c; this.instanceRepo = i; this.regRepo = r;
    }

    @Transactional
    public Registration register(RegistrationDTO dto, String actor) {
        Course course = courseRepo.findById(dto.courseId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        CourseInstance instance = null;
        if (dto.courseInstanceId() != null) {
            instance = instanceRepo.findById(dto.courseInstanceId())
                .orElseThrow(() -> new IllegalArgumentException("Course instance not found"));
        }
        Registration reg = new Registration();
        reg.setCourse(course); reg.setCourseInstance(instance);
        reg.setFullName(dto.fullName()); reg.setEmail(dto.email());
        reg.setPhone(dto.phone()); reg.setOrganization(dto.organization()); reg.setRole(dto.role());
        reg.setPaymentOption(dto.paymentOption()); reg.setPaymentStatus("pending");
        reg.setCreatedBy(actor); reg.setUpdatedBy(actor);
        return regRepo.save(reg);
    }
}
