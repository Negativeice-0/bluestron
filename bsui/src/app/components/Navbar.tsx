'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';

export default function Navbar() {
  return (
    <motion.nav
      initial={{ y: -20, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      className="bg-purple-500 text-white shadow-lg"
    >
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        {/* Logo / Brand */}
        <Link href="/" className="text-2xl font-bold tracking-wide hover:text-orange-300">
          Bluestron
        </Link>

        {/* Navigation Links */}
        <div className="hidden md:flex space-x-6 font-medium">
          <Link href="/about" className="hover:text-orange-300">About</Link>
          <Link href="/courses" className="hover:text-orange-300">Courses</Link>
          <Link href="/research" className="hover:text-orange-300">Research</Link>
          <Link href="/data" className="hover:text-orange-300">Data</Link>
          <Link href="/software" className="hover:text-orange-300">Software</Link>
          <Link href="/blog" className="hover:text-orange-300">Blog</Link>
          <Link href="/contact" className="hover:text-orange-300">Contact</Link>
        </div>

        {/* CTA Button */}
        <Link
          href="/courses"
          className="ml-6 bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-lg font-semibold shadow-md transition"
        >
          Register Now
        </Link>
      </div>
    </motion.nav>
  );
}
