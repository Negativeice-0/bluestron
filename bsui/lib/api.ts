// lib/api.ts
import { Category } from '@/types/category';
import { Course } from '@/types/course';

export async function getCategories(): Promise<Category[]> {
  const res = await fetch(`${process.env.API_URL}/api/categories`, {
    cache: 'no-store',
  });
  if (!res.ok) throw new Error('Failed to fetch categories');
  return res.json();
}

export async function getCategoryBySlug(slug: string): Promise<Category> {
  const res = await fetch(`${process.env.API_URL}/api/categories/${slug}`, {
    cache: 'no-store',
  });
  if (!res.ok) throw new Error('Category not found');
  return res.json();
}

// course api
export async function getCourse(slug: string): Promise<Course> {
  const res = await fetch(`${process.env.API_URL}/api/courses/${slug}`, {
    cache: 'no-store',
  });
  if (!res.ok) throw new Error('Course not found');
  return res.json();
}

// get courses
export async function getCourses(): Promise<Course[]> {
  const res = await fetch(`${process.env.API_URL}/api/courses`, {
    cache: 'no-store',
  });
  if (!res.ok) throw new Error('Failed to fetch courses');
  return res.json();
}
