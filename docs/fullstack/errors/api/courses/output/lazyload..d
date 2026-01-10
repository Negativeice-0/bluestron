That stack trace is the classic **LazyInitializationException** problem:  

Spring/JPA is trying to serialize your `Course` entity to JSON, but the `learningOutcomes` collection is marked as `@ElementCollection` with default `FetchType.LAZY`. By the time Jackson tries to write the JSON, the Hibernate session is closed, so it can’t initialize the proxy → `HttpMessageNotWritableException`.

---

## 🔎 Why curl POST works but GET fails
- **POST**: You send JSON, Spring deserializes it into a DTO, saves it, and returns the entity. At that moment, `learningOutcomes` is still attached to the persistence context, so it may serialize fine or be null.
- **GET**: You fetch a `Course` from the repository. The session closes before Jackson serializes the lazy collection. Jackson then tries to access `learningOutcomes` → fails.

---

## 🛠 Fix options

### 1. Eager fetch for `learningOutcomes`
```java
@ElementCollection(fetch = FetchType.EAGER)
@CollectionTable(name = "course_outcomes", joinColumns = @JoinColumn(name = "course_id"))
@Column(name = "outcome")
private List<String> learningOutcomes;
```
👉 This forces Hibernate to load outcomes immediately, so JSON serialization works.

---

### 2. Use DTOs for responses (best practice)
Instead of returning the entity directly, map it to a DTO that copies the fields you want:
```java
public record CourseResponse(
    Long id,
    String slug,
    String title,
    String shortDescription,
    String fullDescription,
    List<String> learningOutcomes,
    String thumbnailUrl,
    String status,
    CourseCategory category
) {}
```

In controller:
```java
@GetMapping("/{slug}")
public ResponseEntity<CourseResponse> get(@PathVariable String slug) {
    Course c = service.getBySlug(slug);
    return ResponseEntity.ok(new CourseResponse(
        c.getId(), c.getSlug(), c.getTitle(),
        c.getShortDescription(), c.getFullDescription(),
        c.getLearningOutcomes(), c.getThumbnailUrl(),
        c.getStatus(), c.getCategory()
    ));
}
```

👉 This way, you control serialization and avoid lazy proxy issues.

---

### 3. Annotate with `@JsonIgnore` if you don’t need outcomes yet
```java
@JsonIgnore
private List<String> learningOutcomes;
```
👉 Quick fix if you don’t want to expose outcomes in API responses yet.

---

## ✅ Recommended path for Bluestron
- Use **DTOs** for all API responses (option 2). This avoids leaking JPA internals and gives you control over what the frontend sees.
- For now, if you just want to unblock testing, set `fetch = FetchType.EAGER` on `learningOutcomes`.

---

## Next step
Update your `Course` entity with `fetch = FetchType.EAGER` or switch the controller to return a DTO. Then retry:

```bash
curl http://localhost:8080/api/courses/fundamentals-project-management
```

👉 That should now return JSON without the lazy init error.

Would you like me to **refactor the CourseController to use DTO responses** right now, so you can avoid similar issues for Registrations, Trainers, Testimonials, etc.?