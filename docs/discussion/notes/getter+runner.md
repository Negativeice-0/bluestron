runner has some commented code, but it migration (what defined must have) is basically the schema and runner is the actual logic to run it all (go to db , read these and connect).

// DTO = React Props Interface
public record CourseDTO(String slug, String title) {}
// Like: interface CourseDTO { slug: string; title: string; }

// Entity = React Component with internal state
public class Course {
    private String slug;
    private String title;
    
    // Getter for DTO conversion
    public CourseDTO toDTO() {
        return new CourseDTO(this.slug, this.title);
    }
    
    // Setter from DTO (like updating from API response)
    public void updateFromDTO(CourseDTO dto) {
        this.slug = dto.slug();
        this.title = dto.title();
        this.touch();  // Update timestamp
    }
}
Why DTOs use Records (no getters/setters):
Records = Immutable data transfer (like React props - can't change)

Entities = Mutable business objects (like React state - can change)