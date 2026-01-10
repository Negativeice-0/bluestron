Great — let’s finish the **Courses UI page** so you can see the full end‑to‑end flow for courses, just like we did for categories.

---

## Frontend: Courses page

```tsx
// bsui/src/app/courses/page.tsx
'use client'
import { useEffect, useState } from 'react'
import Navbar from '@/components/Navbar'
import Footer from '@/components/Footer'

interface Course {
  id: number
  slug: string
  title: string
  shortDescription?: string
  fullDescription?: string
  thumbnailUrl?: string
  status: string
  category: { id: number; name: string }
}

export default function CoursesPage() {
  const [courses, setCourses] = useState<Course[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/courses')
      .then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        return res.json()
      })
      .then(setCourses)
      .catch(err => setError(err.message))
  }, [])

  return (
    <div className="min-h-screen flex flex-col bg-softwhite text-navy">
      <Navbar />
      <main className="flex-1 px-4 py-10 max-w-5xl mx-auto">
        <h1 className="text-2xl md:text-3xl font-bold mb-6">Courses</h1>
        {error && (
          <div className="p-4 bg-red-100 text-red-700 rounded-md mb-4">
            Failed to load courses: {error}
          </div>
        )}
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          {courses.map(course => (
            <div key={course.id} className="p-6 border border-navy/10 rounded-lg shadow-sm hover:shadow-md transition">
              {course.thumbnailUrl && (
                <img src={course.thumbnailUrl} alt={course.title} className="w-full h-40 object-cover rounded-md mb-4" />
              )}
              <h2 className="text-xl font-semibold mb-2">{course.title}</h2>
              <p className="text-navy/70 mb-2">{course.shortDescription || 'No description provided.'}</p>
              <span className={`inline-block px-2 py-1 text-xs rounded-md ${
                course.status === 'published' ? 'bg-green-100 text-green-700' : 'bg-gray-200 text-gray-600'
              }`}>
                {course.status}
              </span>
              <div className="mt-4">
                <a href={`/courses/${course.slug}`} className="px-3 py-2 bg-orange text-white rounded-md text-sm">
                  View details
                </a>
              </div>
            </div>
          ))}
        </div>
      </main>
      <Footer />
    </div>
  )
}
```

---

## Verification workflow

1. **Run migration**  
   ```bash
   make migrate
   ```
   → Creates `course` table.

2. **Start backend**  
   ```bash
   make api-dev
   ```

3. **Create course via curl**  
   ```bash
   curl -X POST http://localhost:8080/api/courses \
     -H "Content-Type: application/json" \
     -d '{
       "slug": "advanced-me-health-programs",
       "title": "Advanced M&E and Data Management for Health Programs",
       "shortDescription": "Deep-dive into M&E for health.",
       "fullDescription": "Full syllabus...",
       "learningOutcomes": ["Design M&E frameworks", "Analyze health data"],
       "thumbnailUrl": "https://example.com/img/health-me.png",
       "categoryId": 1,
       "status": "published"
     }'
   ```

4. **List courses via curl**  
   ```bash
   curl http://localhost:8080/api/courses
   ```

5. **Get course by slug**  
   ```bash
   curl http://localhost:8080/api/courses/advanced-me-health-programs
   ```

6. **Check DB directly**  
   ```bash
   psql -U bsapi_user -d bsdb -c "SELECT id, slug, title, status FROM course;"
   ```

7. **Run frontend**  
   ```bash
   make ui-dev
   ```
   → Visit `http://localhost:3000/courses`.  
   → Page shows course cards with title, description, thumbnail, status badge, and “View details” button.

---

## Next step

- Add **Course detail page** (`/courses/[slug]`) to show full description, learning outcomes, and a **Register** button.  
- Then implement **CourseInstance** (cohorts) and **Registration** entities with their controllers, so the detail page can display upcoming sessions and allow enrolment.

Would you like me to scaffold the **Course detail page + Registration flow** next, so you can click “View details” and actually register through the UI?