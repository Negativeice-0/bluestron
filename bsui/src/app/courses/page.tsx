// src/app/courses/page.tsx
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
  // Use union type and parse value safely to fix TS error
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
    const matchesCategory = selectedCategory !== '' ? c.category.id === Number(selectedCategory) : true;
    const matchesSearch = c.title.toLowerCase().includes(search.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  return (
    <div className="min-h-screen bg-white/80">
      {/* Header */}
      <section className="p-6 bg-[#000080] text-white">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold">Courses</h1>
          <p className="text-sm opacity-90">Browse by category, search, and register.</p>
        </div>
      </section>

      {/* Controls */}
      <section className="bg-white">
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
            onChange={(e) => {
              const val = e.target.value;
              setSelectedCategory(val === '' ? '' : Number(val));
            }}
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
              className="bg-white border border-[#000080]/10 rounded-lg shadow-sm hover:shadow-md transition p-4"
            >
              <h2 className="text-base font-semibold text-[#000080]">{course.title}</h2>
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
                  className="flex-1 text-center bg-[#000080] text-white py-2 rounded hover:bg-blue-900 transition text-sm"
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
      <section className="p-6 bg-[#000080] text-white text-center">
        <p className="text-lg">Explore upcoming cohorts and featured courses—new sessions added regularly.</p>
      </section>
    </div>
  );
}
