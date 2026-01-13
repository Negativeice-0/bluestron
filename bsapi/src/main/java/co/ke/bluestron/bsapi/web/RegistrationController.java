package co.ke.bluestron.bsapi.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.Registration;
import co.ke.bluestron.bsapi.repository.RegistrationRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationRepository repository;

    public RegistrationController(RegistrationRepository repo) {
        this.repository = repo;
    }

    @PostMapping
    public Registration create(@Valid @RequestBody Registration registration) {
        // The moment a learner signs up – a new journey begins
        return repository.save(registration);
    }
}
