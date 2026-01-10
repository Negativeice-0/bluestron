'use client'
import { useEffect, useState } from 'react'

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
                // eslint-disable-next-line @next/next/no-img-element
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
    </div>
  )
}
