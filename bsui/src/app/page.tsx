'use client';

import { motion } from 'framer-motion';
import Image from 'next/image';
import Link from 'next/link';

export default function HomePage() {
  return (
    <div className="container mx-auto px-6 py-12">
      {/* Hero Section */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="text-center"
      >
        <h1 className="text-5xl font-bold text-purple-500 mb-6">
          Professional Training, Research & Software Development
        </h1>
        <p className="text-lg text-gray-700 mb-8">
          Bluestron delivers expert training and services in data, M&E, and management.
        </p>
        <div className="flex justify-center">
          <Image
            src="/hero.jpg"
            alt="Bluestron Hero"
            width={800}
            height={400}
            className="rounded-lg shadow-lg"
          />
        </div>
        <div className="mt-10 flex justify-center space-x-4">
          <Link href="/courses" className="bg-orange-500 text-white px-6 py-3 rounded-lg font-semibold shadow-md">
            Register Now
          </Link>
          <Link href="/contact" className="bg-blue-500 text-white px-6 py-3 rounded-lg font-semibold shadow-md">
            Enquire
          </Link>
          <Link href="/about" className="bg-purple-500 text-white px-6 py-3 rounded-lg font-semibold shadow-md">
            Learn More
          </Link>
        </div>
      </motion.div>

      {/* Highlight Boxes */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-16">
        <div className="bg-white rounded-xl shadow-lg p-6 border hover:shadow-xl transition">
          <h2 className="text-2xl font-semibold text-blue-500 mb-2">Training</h2>
          <p className="text-gray-600">Browse our professional courses and register today.</p>
          <Link href="/courses" className="text-orange-500 font-semibold mt-4 inline-block">Register →</Link>
        </div>
        <div className="bg-white rounded-xl shadow-lg p-6 border hover:shadow-xl transition">
          <h2 className="text-2xl font-semibold text-blue-500 mb-2">Research Services</h2>
          <p className="text-gray-600">Baseline, midline, endline evaluations and consultancy.</p>
          <Link href="/research" className="text-orange-500 font-semibold mt-4 inline-block">Enquire →</Link>
        </div>
        <div className="bg-white rounded-xl shadow-lg p-6 border hover:shadow-xl transition">
          <h2 className="text-2xl font-semibold text-blue-500 mb-2">Software Development</h2>
          <p className="text-gray-600">Custom apps, dashboards, and integrations.</p>
          <Link href="/software" className="text-orange-500 font-semibold mt-4 inline-block">Learn More →</Link>
        </div>
      </div>
    </div>
  );
}
