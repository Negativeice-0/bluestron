import { getCourses } from '@/lib/api';
import { Course } from '@/types/course';
import CourseCard from '../components/CourseCard';

export default async function CoursesPage() {
  const courses: Course[] = await getCourses();

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold text-purple-500 mb-8">
        All Courses
      </h1>

      {/* Search + Filter UI (placeholder for now) */}
      <div className="mb-6 flex gap-4">
        <input
          type="text"
          placeholder="Search courses..."
          className="flex-1 border rounded p-2"
        />
        <select className="border rounded p-2">
          <option value="">All Categories</option>
          <option value="featured">Featured</option>
          <option value="free">Free</option>
        </select>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {courses.map((course) => (
          <CourseCard key={course.id} course={course} />
        ))}
      </div>
    </div>
  );
}
