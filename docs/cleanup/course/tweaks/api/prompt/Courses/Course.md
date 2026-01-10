give me getters + setters, plus comment on what they do (correct and use this analogy -- frontend has edit + delete button; backend has getter and setter) and the aggregate these into this code:"// bsapi/src/main/java/co/ke/bluestron/bsapi/entity/Course.java
package co.ke.bluestron.bsapi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * Course entity belongs to a CourseCategory.
 * Includes audit fields for admin tracking.
 */
@Entity
@Table(name = "course")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @ElementCollection
    @CollectionTable(name = "course_outcomes", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "outcome")
    private List<String> learningOutcomes;

    private String thumbnailUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @Column(nullable = false, length = 20)
    private String status = "draft";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    private String createdBy;
    private String updatedBy;

    // getters/setters omitted for brevity
}
"