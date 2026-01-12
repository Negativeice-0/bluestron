### Courses page scaffold

```tsx
// src/app/courses/page.tsx
'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';

interface Category {
  id: number;
  name: string;
  slug: string;
}

interface Course {
  id: number;
  category: Category;
  title: string;
  slug: string;
  shortDescription: string;
  duration: string;
  mode: 'IN_PERSON' | 'ONLINE' | 'HYBRID';
  price: number;
}

export default function CoursesPage() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [search, setSearch] = useState<string>('');
  const [selectedCategory, setSelectedCategory] = useState<number | ''>('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [coursesRes, categoriesRes] = await Promise.all([
          fetch('http://localhost:8080/api/courses'),
          fetch('http://localhost:8080/api/categories'),
        ]);
        if (coursesRes.ok) setCourses(await coursesRes.json());
        if (categoriesRes.ok) setCategories(await categoriesRes.json());
      } catch (err) {
        console.error('Fetch error:', err);
      }
    };
    fetchData();
  }, []);

  const filtered = courses.filter((c) => {
    const matchesCategory = selectedCategory ? c.category.id === Number(selectedCategory) : true;
    const matchesSearch = c.title.toLowerCase().includes(search.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <section className="p-6 bg-blue-900 text-white">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold">Courses</h1>
          <p className="text-sm opacity-90">Browse by category, search, and register.</p>
        </div>
      </section>

      {/* Controls */}
      <section className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto p-6 flex flex-col md:flex-row gap-4">
          <input
            type="text"
            placeholder="Search courses..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="flex-1 p-2 border rounded focus:outline-orange-500"
          />
          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
            className="p-2 border rounded focus:outline-orange-500"
          >
            <option value="">All Categories</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>
        </div>
      </section>

      {/* Grid */}
      <section className="max-w-7xl mx-auto p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filtered.map((course) => (
            <article
              key={course.id}
              className="bg-white border border-blue-900/10 rounded-lg shadow-sm hover:shadow-md transition p-4"
            >
              <h2 className="text-lg font-semibold text-blue-900">{course.title}</h2>
              <p className="text-gray-600 mb-2 text-sm">{course.shortDescription}</p>
              <div className="text-xs text-gray-500 space-y-1 mb-3">
                <p>Category: {course.category.name}</p>
                <p>Duration: {course.duration}</p>
                <p>Mode: {course.mode}</p>
                <p>Price: KES {course.price}</p>
              </div>
              <div className="flex gap-2">
                <Link
                  href={`/courses/${course.slug}`}
                  className="flex-1 text-center bg-blue-900 text-white py-2 rounded hover:bg-blue-800 transition text-sm"
                >
                  View details
                </Link>
                <button
                  className="flex-1 bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition text-sm"
                  onClick={() => alert(`Register for ${course.title}`)}
                >
                  Register
                </button>
              </div>
            </article>
          ))}
        </div>
      </section>

      {/* Banner */}
      <section className="p-6 bg-blue-900 text-white text-center">
        <p className="text-lg">Explore upcoming cohorts and featured courses—new sessions added regularly.</p>
      </section>
    </div>
  );
}
```

---

### Home page scaffold (aligned to Bluestron requirements)

```tsx
// src/app/page.tsx
'use client';

import Link from 'next/link';
import { useEffect, useState } from 'react';

interface Category {
  id: number;
  name: string;
  slug: string;
}

interface Course {
  id: number;
  title: string;
  slug: string;
  shortDescription: string;
  featured: boolean;
}

export default function HomePage() {
  const [featured, setFeatured] = useState<Course[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [coursesRes, categoriesRes] = await Promise.all([
          fetch('http://localhost:8080/api/courses'),
          fetch('http://localhost:8080/api/categories'),
        ]);
        if (coursesRes.ok) {
          const courses: Course[] = await coursesRes.json();
          setFeatured(courses.filter((c) => c.featured).slice(0, 6));
        }
        if (categoriesRes.ok) setCategories(await categoriesRes.json());
      } catch (err) {
        console.error('Home fetch error:', err);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero */}
      <section className="bg-blue-900 text-white">
        <div className="max-w-7xl mx-auto px-6 py-16">
          <h1 className="text-4xl font-bold">Professional Training, Research & Software Development</h1>
          <p className="mt-3 text-sm opacity-90">
            Bluestron delivers high-quality courses and services in Data, M&E, and Management.
          </p>
          <div className="mt-6 flex gap-3">
            <Link href="/courses" className="px-5 py-2 bg-orange-500 rounded text-white hover:bg-orange-600 transition">
              Browse courses
            </Link>
            <Link href="/contact" className="px-5 py-2 bg-white text-blue-900 rounded hover:bg-gray-100 transition">
              Enquire
            </Link>
          </div>
        </div>
      </section>

      {/* Three-box highlights */}
      <section className="max-w-7xl mx-auto px-6 py-10 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white border border-blue-900/10 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-blue-900">Training</h3>
          <p className="text-gray-600 text-sm">Courses across Project Management, Data Analysis, M&E, GIS & IT.</p>
          <Link href="/courses" className="inline-block mt-3 text-orange-500 hover:text-orange-600 text-sm">
            Learn more →
          </Link>
        </div>
        <div className="bg-white border border-blue-900/10 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-blue-900">Research Services</h3>
          <p className="text-gray-600 text-sm">Baseline, midline, endline evaluations and custom consultancy.</p>
          <Link href="/services/research" className="inline-block mt-3 text-orange-500 hover:text-orange-600 text-sm">
            Learn more →
          </Link>
        </div>
        <div className="bg-white border border-blue-900/10 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-blue-900">Software Development</h3>
          <p className="text-gray-600 text-sm">Custom web/mobile apps, integrations, automation, dashboards.</p>
          <Link href="/services/software" className="inline-block mt-3 text-orange-500 hover:text-orange-600 text-sm">
            Learn more →
          </Link>
        </div>
      </section>

      {/* Featured courses carousel (simple grid) */}
      <section className="max-w-7xl mx-auto px-6 py-10">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-bold text-blue-900">Featured & Upcoming Courses</h2>
          <Link href="/courses" className="text-orange-500 hover:text-orange-600 text-sm">
            View all →
          </Link>
        </div>
        <div className="mt-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {featured.map((c) => (
            <article key={c.id} className="bg-white border border-blue-900/10 rounded-lg p-4 shadow-sm">
              <h3 className="text-lg font-semibold text-blue-900">{c.title}</h3>
              <p className="text-gray-600 text-sm">{c.shortDescription}</p>
              <Link
                href={`/courses/${c.slug}`}
                className="inline-block mt-3 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition text-sm"
              >
                Register now
              </Link>
            </article>
          ))}
        </div>
      </section>

      {/* Quick links to categories */}
      <section className="max-w-7xl mx-auto px-6 py-10">
        <h2 className="text-2xl font-bold text-blue-900">Explore by Category</h2>
        <div className="mt-4 flex flex-wrap gap-3">
          {categories.map((cat) => (
            <Link
              key={cat.id}
              href={`/courses?category=${cat.id}`}
              className="px-3 py-2 bg-white border border-blue-900/10 rounded text-sm hover:bg-gray-100"
            >
              {cat.name}
            </Link>
          ))}
        </div>
      </section>

      {/* Testimonials placeholder */}
      <section className="bg-white border-t border-blue-900/10">
        <div className="max-w-7xl mx-auto px-6 py-10">
          <h2 className="text-2xl font-bold text-blue-900">Testimonials</h2>
          <p className="text-gray-600 text-sm">Success stories coming soon.</p>
        </div>
      </section>
    </div>
  );
}
```

---

### Curl verification commands

- List courses:
  ```
  curl -s http://localhost:8080/api/courses | jq '.[] | {id, title, slug, featured}'
  ```
- List categories:
  ```
  curl -s http://localhost:8080/api/categories | jq '.[] | {id, name, slug}'
  ```
- Get course by slug (adjust endpoint if needed):
  ```
  curl -s http://localhost:8080/api/courses/{slug}
  ```
- Register (example payload—wire later on course detail):
  ```
  curl -X POST http://localhost:8080/api/registrations \
    -H "Content-Type: application/json" \
    -d '{"fullName":"Test User","email":"test@bluestron.co.ke","phone":"+254700000000","organization":"Bluestron","roleDesignation":"Analyst","paymentOption":"INVOICE","courseId":1}'
  ```

---

### Notes for Spring Boot (Spring 3) best practices

- Use `@RestController` + `@RequestMapping("/api")` with clear resource paths (`/courses`, `/categories`, `/registrations`).
- DTOs for requests/responses; avoid exposing entities directly.
- Validation with `jakarta.validation` (`@NotBlank`, `@Email`, etc.) on DTOs.
- Pagination for listings (`Page<CourseDto>`) to keep responses lean.
- Security: permit public GETs for courses/categories; protect POST/PUT/DELETE with `ROLE_ADMIN`.
- CORS: configure allowed origins for `http://localhost:3000`.

---

If you’re good with this, I’ll draft the course detail page next—full description, registration form posting to `/api/registrations`, and a success state that matches Bluestron’s workflow.