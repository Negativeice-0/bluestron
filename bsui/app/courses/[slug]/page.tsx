import { getCourse } from '@/lib/api';
import { Course } from '@/types/course';
import RegistrationForm from '../../components/RegistrationForm';

export default async function CourseDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params;   // ✅ unwrap the promise
  const course: Course = await getCourse(slug);

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="bg-linear-to-r from-purple-500 to-blue-500 text-white p-8 rounded-2xl mb-8">
        <h1 className="text-5xl font-bold mb-4">{course.title}</h1>
        <p className="text-xl">{course.description}</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2">
          <div className="bg-white p-6 rounded-xl shadow">
            <h2 className="text-3xl font-bold text-orange-500 mb-4">
              What You will Learn
            </h2>
            {/* TODO: Render course content details */}
          </div>
        </div>

        <div className="lg:col-span-1">
          <RegistrationForm courseId={course.id} />
        </div>
      </div>
    </div>
  );
}
