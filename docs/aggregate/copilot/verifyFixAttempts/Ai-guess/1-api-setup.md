Got it — the 404 you’re seeing isn’t CORS (CORS only affects browser requests). A `404 Not Found` from curl means Spring Boot didn’t register any controller at `/api/categories`. That tells us the backend “grunt work” (controllers, mappings) isn’t wired yet, so curl is hitting an empty path.  

Here’s how to be absolutely sure and how to move forward:

---

### 1. Verify backend endpoints exist
- Start Spring Boot and check the actuator mappings:
  ```bash
  curl http://localhost:8080/actuator/mappings
  ```
  This will list every registered endpoint. If `/api/categories` isn’t there, the controller class isn’t being picked up (package scanning, missing `@RestController`, or wrong `@RequestMapping`).

- Also check Swagger UI:
  ```
  http://localhost:8080/swagger-ui/index.html
  ```
  If categories aren’t listed, the controller isn’t implemented yet.

---

### 2. Minimal controller to unblock curl
Add a simple controller in `co.ke.bluestron.bsapi.controller`:

```java
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @GetMapping
    public List<String> list() {
        return List.of("stub-category");
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> body) {
        return Map.of("status", "created", "slug", body.get("slug"));
    }
}
```

Rebuild and rerun. Now curl will succeed:
```bash
curl http://localhost:8080/api/categories
```

---

### 3. Why curl is blocked
- **Not CORS:** curl isn’t subject to browser CORS rules.
- **Not Spring Security:** you disabled CSRF and permitted `/api/**` in the stub config. If you hadn’t, you’d see `401 Unauthorized`, not `404`.
- **Actual cause:** missing controller mappings.

---

### 4. Frontend testing strategy
Yes, implementing the frontend to test the backend is a good move. Next.js can consume the API directly:

- Create a simple fetch in `bsui/src/app/categories/page.tsx`:

```tsx
'use client'
import { useEffect, useState } from 'react'

export default function CategoriesPage() {
  const [categories, setCategories] = useState<any[]>([])

  useEffect(() => {
    fetch('http://localhost:8080/api/categories')
      .then(res => res.json())
      .then(setCategories)
      .catch(err => console.error(err))
  }, [])

  return (
    <section className="p-4">
      <h1 className="text-xl font-bold">Categories</h1>
      <ul>
        {categories.map((c, i) => (
          <li key={i}>{JSON.stringify(c)}</li>
        ))}
      </ul>
    </section>
  )
}
```

- Run `npm run dev` in `bsui` and visit `http://localhost:3000/categories`.  
  If the backend responds, you’ll see data. If not, you’ll see errors in the console — confirming the backend isn’t serving that path.

---

### 5. Next step
Before we proceed to Module 2 (auth/admin), we need to **lock Module 1 controllers** so curl and frontend both succeed. That means:
- Implement `CategoryController`, `CourseController`, `RegistrationController`, etc. with `@RestController` and `@RequestMapping`.
- Verify with `curl` and `fetch` from frontend.
- Only then layer authentication.

---

👉 Suggestion: let’s wire **CategoryController** fully (CRUD with JPA repository) so you can run curl again and see a real DB insert. Once that works, we’ll replicate the pattern for Courses, Registrations, Services, Blog, Trainers, Testimonials, FeaturedContent.  

Do you want me to generate the **full CategoryController + Repository + Service + DTO** right now so you can test curl end-to-end?


This changes every boot
default spring security
username: user
Using generated security password: d9149cb7-f1dd-4868-a0da-d14e2ef680b6

Though using frontend avoids this.


