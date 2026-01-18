//import { motion } from "framer-motion";
import { Course } from "@/types";

// Explicitly typing the fetcher return
async function getFeaturedCourses(): Promise<Course[]> {
  try {
    const res = await fetch('http://localhost:8080/api/courses', { 
      cache: 'no-store' 
    });
    
    if (!res.ok) throw new Error('Failed to fetch courses');
    
    const data: Course[] = await res.json();
    
    // Filter logic as requested by Bluestron PDF
    return data.filter((course: Course) => course.isFeatured);
  } catch (error) {
    console.error("Data fetch error:", error);
    return [];
  }
}

export default async function HomePage() {
  const featuredCourses = await getFeaturedCourses();

  return (
    <main className="min-h-screen bg-white">
      {/* HERO SECTION */}
      <section className="relative h-150 flex items-center justify-center bg-linear-to-r from-blue-900 to-indigo-800 text-white">
        <div className="container mx-auto px-6 text-center">
          <h1 className="text-5xl font-bold mb-4">Professional Training, Research & Software Development</h1>
          <p className="text-xl mb-8 opacity-90">Empowering organizations through Data, M&E, and Management expertise.</p>
          <div className="flex justify-center gap-4">
            <button className="bg-orange-500 hover:bg-orange-600 px-8 py-3 rounded-lg font-semibold transition cursor-pointer">
              Explore Courses
            </button>
            <button className="border border-white hover:bg-white hover:text-blue-900 px-8 py-3 rounded-lg font-semibold transition cursor-pointer">
              Our Services
            </button>
          </div>
        </div>
      </section>

      {/* 3-BOX HIGHLIGHTS (Bluestron PDF Page 1) */}
      <section className="container mx-auto -mt-16 px-6 grid md:grid-cols-3 gap-8">
        {[
          { title: "Expert Training", desc: "Short courses in GIS, Data Analysis, and PM." },
          { title: "Research Services", desc: "Baseline, Midline, and Endline evaluations." },
          { title: "Software Dev", desc: "Custom digital solutions and data collection tools." }
        ].map((box, i) => (
          <div key={i} className="bg-white p-8 rounded-xl shadow-lg border-t-4 border-blue-600">
            <h3 className="text-xl font-bold mb-2 text-blue-900">{box.title}</h3>
            <p className="text-gray-600">{box.desc}</p>
          </div>
        ))}
      </section>

      {/* FEATURED COURSES - Strictly Typed */}
      <section className="py-20 container mx-auto px-6">
        <h2 className="text-3xl font-bold mb-10 text-center text-gray-800">Featured Courses</h2>
        
        {featuredCourses.length === 0 ? (
          <p className="text-center text-gray-500">No featured courses available at the moment.</p>
        ) : (
          <div className="grid md:grid-cols-4 gap-6">
            {featuredCourses.map((course: Course) => (
              <div key={course.id} className="group border border-gray-100 rounded-xl overflow-hidden hover:shadow-xl transition flex flex-col bg-white">
                <div className="h-48 bg-gray-100 flex items-center justify-center text-gray-400">
                  {/* Tailwind 4 uses standard CSS logic for aspect ratios if preferred */}
                  <span>Image Placeholder</span>
                </div>
                <div className="p-4 grow flex flex-col">
                  <h4 className="font-bold text-lg mb-2 group-hover:text-blue-600 transition truncate">
                    {course.title}
                  </h4>
                  <p className="text-sm text-gray-500 line-clamp-2 mb-4">
                    {course.description}
                  </p>
                  <div className="mt-auto">
                    <div className="text-blue-700 font-bold mb-4">
                      {course.price > 0 ? `USD ${course.price}` : 'Contact for Pricing'}
                    </div>
                    <button className="w-full py-2 bg-blue-50 text-blue-700 hover:bg-blue-600 hover:text-white rounded-lg font-medium transition cursor-pointer">
                      Enroll Now
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </main>
  );
}