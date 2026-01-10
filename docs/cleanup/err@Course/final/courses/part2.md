Perfect — now that you’ve confirmed the backend is inserting courses successfully, let’s tighten up the **Courses UI page** so it’s stricter TypeScript, modern Next.js best practices, and styled with Bluestron’s vibrant palette (purple + softwhite + orange). This way you can verify end‑to‑end with fresh data and no chance of silent errors.

---

## 🎨 Updated Courses Page (Next.js + TS + Tailwind)

```tsx
// bsui/src/app/courses/page.tsx
'use client'

import { useEffect, useState } from 'react'
import Link from 'next/link'
import Navbar from '@/components/Navbar'
import Footer from '@/components/Footer'

interface Category {
  id: number
  name: string
  slug: string
}

interface Course {
  id: number
  slug: string
  title: string
  shortDescription?: string
  fullDescription?: string
  thumbnailUrl?: string
  status: string
  category: Category
}

export default function CoursesPage() {
  const [courses, setCourses] = useState<Course[]>([])
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    fetch('http://localhost:8080/api/courses')
      .then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        return res.json()
      })
      .then(data => setCourses(data))
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="min-h-screen flex flex-col bg-softwhite text-purple-900">
      <Navbar />
      <main className="flex-1 px-4 py-10 max-w-6xl mx-auto">
        <h1 className="text-3xl md:text-4xl font-extrabold mb-8 text-purple-800">
          Explore Our Courses
        </h1>

        {loading && <p className="text-orange">Loading courses...</p>}
        {error && (
          <div className="p-4 bg-red-100 text-red-700 rounded-md mb-4">
            Failed to load courses: {error}
          </div>
        )}

        <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3">
          {courses.map(course => (
            <div
              key={course.id}
              className="p-6 border border-purple-200 rounded-lg shadow hover:shadow-lg transition bg-white"
            >
              {course.thumbnailUrl && (
                <img
                  src={course.thumbnailUrl}
                  alt={course.title}
                  className="w-full h-40 object-cover rounded-md mb-4"
                />
              )}
              <h2 className="text-xl font-semibold mb-2 text-purple-900">
                {course.title}
              </h2>
              <p className="text-purple-700 mb-3">
                {course.shortDescription || 'No description provided.'}
              </p>
              <span
                className={`inline-block px-3 py-1 text-xs font-medium rounded-md ${
                  course.status === 'published'
                    ? 'bg-orange text-white'
                    : 'bg-gray-200 text-gray-600'
                }`}
              >
                {course.status}
              </span>
              <div className="mt-4">
                <Link
                  href={`/courses/${course.slug}`}
                  className="px-4 py-2 bg-purple-700 text-softwhite rounded-md text-sm hover:bg-purple-800 transition"
                >
                  View details
                </Link>
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

## 🧪 Fresh Data for Verification
# !bash
make api-dev



1. **Create a new category**  
   ```bash
   curl -X POST http://localhost:8080/api/categories \
     -H "Content-Type: application/json" \
     -d '{
       "slug": "data-analysis",
       "name": "Data Analysis & Management",
       "description": "Courses on data cleaning, visualization, and analytics",
       "status": "active"
     }'
   ```

2. **Create a new course** (linked to that category’s ID, say `4`):  
   ```bash
   curl -X POST http://localhost:8080/api/courses \
     -H "Content-Type: application/json" \
     -d '{
       "slug": "power-bi-essentials",
       "title": "Power BI Essentials",
       "shortDescription": "Learn to build dashboards and reports with Power BI.",
       "fullDescription": "This course covers data modeling, visualization, and publishing interactive dashboards using Power BI.",
       "thumbnailUrl": "https://example.com/img/power-bi.png",
       "categoryId": 4,
       "status": "published"
     }'
   ```

3. **List courses**  
   ```bash
   curl http://localhost:8080/api/courses
   ```

4. **Get course by slug**  
   ```bash
   curl http://localhost:8080/api/courses/power-bi-essentials
   ```

5. **Run frontend**  
   ```bash
   make ui-dev
   ```
   → Visit `http://localhost:3000/courses`.  
   → You should see vibrant purple/orange/softwhite cards with your new course.

---

## 🚀 Next step

Now that the **Courses page** is strict TypeScript and styled properly, the next logical piece is the **Course detail page** (`/courses/[slug]`) with a **Register Now** button. That will close the loop from backend → curl → UI → registration.  

Would you like me to scaffold the **Course detail page + Registration form** next so you can actually test enrolments end‑to‑end?