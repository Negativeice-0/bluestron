import RegistrationForm from '@/components/RegistrationForm';
import { Course } from '@/types';

async function getCourse(slug: string): Promise<Course> {
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/courses/${slug}`, {
    cache: 'no-store',
  });
  if (!res.ok) {
    throw new Error(`Failed to fetch course: ${res.statusText}`);
  }
  return res.json();
}

export default async function CourseDetailPage({ params }: { params: { slug: string } }) {
  const course = await getCourse(params.slug);

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Hero */}
      <div className="bg-linear-to-r from-purple-500 to-blue-500 text-white p-8 rounded-2xl mb-8">
        <h1 className="text-5xl font-bold mb-4">{course.title}</h1>
        <p className="text-xl">{course.description}</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Course content */}
        <div className="lg:col-span-2">
          <div className="bg-white p-6 rounded-xl shadow">
            <h2 className="text-3xl font-bold text-orange-500 mb-4">What You will Learn</h2>
            <ul className="list-disc list-inside space-y-2 text-gray-700">
              {course.learningOutcomes?.map((outcome, idx) => (
                <li key={idx}>{outcome}</li>
              ))}
            </ul>
          </div>
        </div>

        {/* Inline registration form */}
        <div className="lg:col-span-1">
          <RegistrationForm course={course} />
        </div>
      </div>
    </div>
  );
}
