// src/app/components/Navbar.tsx
'use client';

import { motion } from 'framer-motion';
import Image from 'next/image';
import Link from 'next/link';
import { useState } from 'react';

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);

  const navLinks = [
    { name: 'Home', href: '/' },
    { name: 'Courses', href: '/courses' },
    { name: 'Services', href: '/services' },
    { name: 'Blog', href: '/blog' },
    { name: 'Contact', href: '/contact' },
  ];

  return (
    <header className="px-4 py-3 border-b border-[#000080]/10 sticky top-0 bg-white backdrop-blur-md z-50">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        {/* Logo */}
        <Link href="/" className="flex items-center gap-2 hover:opacity-90 transition-opacity">
          <div className="relative h-8 w-8">
            <Image src="/logo.svg" alt="Bluestron Logo" fill priority className="object-contain" />
          </div>
          <div className="flex items-center font-bold text-lg tracking-tight">
            <span className="text-lime-500">- </span>
            <span className="text-[#000080]">Blue</span>
            <span className="text-orange-500">stron</span>
          </div>
        </Link>

        {/* Desktop nav */}
        <div className="hidden md:flex items-center gap-8">
          <nav className="flex gap-6 text-sm font-medium">
            {navLinks.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className="text-[#000080] hover:text-orange-500 transition-colors"
              >
                {link.name}
              </Link>
            ))}
          </nav>

          {/* Pulsating Gradient Login Button */}
          <motion.div
            animate={{ scale: [1, 1.05, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            <Link
              href="/login"
              className="px-5 py-2 text-sm font-semibold text-white rounded-full bg-linear-to-r from-[#000080] to-orange-500 shadow-lg"
            >
              Log In
            </Link>
          </motion.div>
        </div>

        {/* Mobile toggle */}
        <button
          className="md:hidden p-2 text-[#000080]"
          onClick={() => setIsOpen(!isOpen)}
          aria-label="Toggle menu"
        >
          <div className="w-6 h-5 flex flex-col justify-between">
            <span className={`h-0.5 w-full bg-current transition-all ${isOpen ? 'rotate-45 translate-y-2' : ''}`} />
            <span className={`h-0.5 w-full bg-current transition-all ${isOpen ? 'opacity-0' : ''}`} />
            <span className={`h-0.5 w-full bg-current transition-all ${isOpen ? '-rotate-45 -translate-y-2' : ''}`} />
          </div>
        </button>
      </div>

      {/* Mobile menu */}
      {isOpen && (
        <nav className="md:hidden flex flex-col gap-4 px-4 py-4 bg-white">
          {navLinks.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              onClick={() => setIsOpen(false)}
              className="text-[#000080] hover:text-orange-500"
            >
              {link.name}
            </Link>
          ))}
          <motion.div
            animate={{ scale: [1, 1.05, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            <Link
              href="/login"
              className="mt-2 text-center py-3 bg-linear-to-r from-[#000080] to-orange-500 text-white rounded-xl font-bold shadow-lg"
            >
              Log In
            </Link>
          </motion.div>
        </nav>
      )}
    </header>
  );
}
