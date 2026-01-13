import { Course } from '@/types/course';
import Link from 'next/link';

export default function CourseCard({ course }: { course: Course }) {
  return (
    <Link href={`/courses/${course.slug}`}>
      <div className="bg-white rounded-xl shadow-lg p-6 border border-blue-100 hover:shadow-xl transition-shadow duration-300">
        <h2 className="text-2xl font-semibold text-blue-500 mb-2">
          {course.title}
        </h2>
        <p className="text-gray-600">{course.description}</p>
        {course.price && (
          <p className="text-purple-600 font-bold mt-2">${course.price}</p>
        )}
      </div>
    </Link>
  );
}
