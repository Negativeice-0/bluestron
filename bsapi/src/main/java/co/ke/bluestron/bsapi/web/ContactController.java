package co.ke.bluestron.bsapi.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.ContactSubmission;
import co.ke.bluestron.bsapi.repository.ContactSubmissionRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactSubmissionRepository repository;
    
    public ContactController(ContactSubmissionRepository repo) {
        this.repository = repo;
    }
    
    @PostMapping
    public ContactSubmission submit(@Valid @RequestBody ContactSubmission submission) {
        // A message in a bottle, cast into the Bluestron sea
        return repository.save(submission);
    }
}
