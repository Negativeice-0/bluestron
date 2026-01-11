'use client';

import { useEffect, useState } from 'react';

interface Course {
  id: number;
  title: string;
  slug: string;
  description: string;
  duration: string;
  mode: string;
  price: number;
}

export default function CoursesPage() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/courses')
      .then(res => res.json())
      .then(data => {
        setCourses(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Failed to fetch courses:', err);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading courses...</div>;

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Training Courses</h1>
      <div style={{ display: 'grid', gap: '1rem', marginTop: '2rem' }}>
        {courses.map(course => (
          <div key={course.id} style={{ 
            border: '1px solid #ccc', 
            padding: '1rem',
            borderRadius: '8px'
          }}>
            <h3>{course.title}</h3>
            <p>{course.description.substring(0, 100)}...</p>
            <div style={{ marginTop: '1rem' }}>
              <span>{course.duration} • {course.mode}</span>
              {course.price && <span style={{ marginLeft: '1rem' }}>${course.price}</span>}
            </div>
            <a href={`/courses/${course.slug}`} style={{ 
              display: 'inline-block', 
              marginTop: '1rem',
              color: 'blue'
            }}>
              View Details
            </a>
          </div>
        ))}
      </div>
    </div>
  );
}