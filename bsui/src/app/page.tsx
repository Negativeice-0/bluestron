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
    <div className="min-h-screen bg-white/80">
      {/* Hero */}
      <section className="bg-[#000080] text-white">
        <div className="max-w-7xl mx-auto px-6 py-16">
          <h1 className="text-4xl font-bold">Professional Training, Research & Software Development</h1>
          <p className="mt-3 text-sm opacity-90">
            Bluestron delivers high-quality courses and services in Data, M&E, and Management.
          </p>
          <div className="mt-6 flex gap-3">
            <Link href="/courses" className="px-5 py-2 bg-orange-500 rounded text-white hover:bg-orange-600 transition">
              Browse courses
            </Link>
            <Link href="/contact" className="px-5 py-2 bg-white text-[#000080] rounded hover:bg-gray-100 transition">
              Enquire
            </Link>
          </div>
        </div>
      </section>

      {/* Three-box highlights */}
      <section className="max-w-7xl mx-auto px-6 py-10 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white border border-[#000080]/10 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-[#000080]">Training</h3>
          <p className="text-gray-600 text-sm">Courses across Project Management, Data Analysis, M&E, GIS & IT.</p>
          <Link href="/courses" className="inline-block mt-3 text-orange-500 hover:text-orange-600 text-sm">
            Learn more →
          </Link>
        </div>
        <div className="bg-white border border-[#000080]/10 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-[#000080]">Research Services</h3>
          <p className="text-gray-600 text-sm">Baseline, midline, endline evaluations and custom consultancy.</p>
          <Link href="/services/research" className="inline-block mt-3 text-orange-500 hover:text-orange-600 text-sm">
            Learn more →
          </Link>
        </div>
        <div className="bg-white border border-[#000080]/10 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-[#000080]">Software Development</h3>
          <p className="text-gray-600 text-sm">Custom web/mobile apps, integrations, automation, dashboards.</p>
          <Link href="/services/software" className="inline-block mt-3 text-orange-500 hover:text-orange-600 text-sm">
            Learn more →
          </Link>
        </div>
      </section>

      {/* Featured courses */}
      <section className="max-w-7xl mx-auto px-6 py-10">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-bold text-[#000080]">Featured & Upcoming Courses</h2>
          <Link href="/courses" className="text-orange-500 hover:text-orange-600 text-sm">
            View all →
          </Link>
        </div>
        <div className="mt-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {featured.map((c) => (
            <article key={c.id} className="bg-white border border-[#000080]/10 rounded-lg p-4 shadow-sm">
              <h3 className="text-base font-semibold text-[#000080]">{c.title}</h3>
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

      {/* Categories quick links */}
      <section className="max-w-7xl mx-auto px-6 py-10">
        <h2 className="text-2xl font-bold text-[#000080]">Explore by Category</h2>
        <div className="mt-4 flex flex-wrap gap-3">
          {categories.map((cat) => (
            <Link
              key={cat.id}
              href={`/courses?category=${cat.id}`}
              className="px-3 py-2 bg-white border border-[#000080]/10 rounded text-sm hover:bg-gray-100"
            >
              {cat.name}
            </Link>
          ))}
        </div>
      </section>

      {/* Testimonials placeholder */}
      <section className="bg-white border-t border-[#000080]/10">
        <div className="max-w-7xl mx-auto px-6 py-10">
          <h2 className="text-2xl font-bold text-[#000080]">Testimonials</h2>
          <p className="text-gray-600 text-sm">Success stories coming soon.</p>
        </div>
      </section>
    </div>
  );
}
