"use client";

import Image from "next/image";
import Link from "next/link";
import { useState } from "react";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);

  const navLinks = [
    { name: "Home", href: "/" },
    { name: "Courses", href: "/courses" },
    { name: "Services", href: "/services" },
    { name: "Blog", href: "/blog" },
    { name: "Contact", href: "/contact" },
  ];

  return (
    <header className="px-4 py-3 border-b border-navy/10 sticky top-0 bg-white/80 backdrop-blur-md z-50">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        {/* Logo Section */}
        <Link href="/" className="flex items-center gap-2 hover:opacity-90 transition-opacity group">
          <div className="relative h-8 w-8 animate-spin-slow">
            <Image src="/logo.svg" alt="Bluestron Logo" fill priority className="object-contain" />
          </div>
          <div className="flex items-center font-bold text-lg tracking-tight">
            <span className="text-lime-500">-</span>
            <span className="text-[#000080]">Blue</span>
            <span className="text-orange-500">stron</span>
          </div>
        </Link>

        {/* Desktop Navigation & Login */}
        <div className="hidden md:flex items-center gap-8">
          <nav className="flex gap-6 text-sm font-medium">
            {navLinks.map((link) => (
              <Link key={link.href} href={link.href} className="transition-colors hover:text-orange-500">
                {link.name}
              </Link>
            ))}
          </nav>
          
          {/* Glowing Login Button */}
          <Link 
            href="/login" 
            className="relative inline-flex items-center justify-center px-5 py-2 text-sm font-semibold text-white transition-all duration-300 rounded-full group bg-linear-to-r from-orange-500 to-[#000080] shadow-lg shadow-orange-500/30 hover:shadow-orange-500/50 active:scale-95"
          >
            <span className="absolute inset-0 w-full h-full transition duration-300 transform scale-105 bg-orange-500 opacity-0 blur-md group-hover:opacity-40 rounded-full"></span>
            <span className="relative">Log In</span>
          </Link>
        </div>

        {/* Mobile Hamburger Button */}
        <button 
          className="md:hidden p-2 text-[#000080]" 
          onClick={() => setIsOpen(!isOpen)}
          aria-label="Toggle menu"
        >
          <div className="w-6 h-5 flex flex-col justify-between">
            <span className={`h-0.5 w-full bg-current transition-all ${isOpen ? "rotate-45 translate-y-2" : ""}`} />
            <span className={`h-0.5 w-full bg-current transition-all ${isOpen ? "opacity-0" : ""}`} />
            <span className={`h-0.5 w-full bg-current transition-all ${isOpen ? "-rotate-45 -translate-y-2" : ""}`} />
          </div>
        </button>
      </div>

      {/* Mobile Menu Dropdown */}
      <div className={`md:hidden overflow-hidden transition-all duration-300 ease-in-out ${isOpen ? "max-h-80 opacity-100 py-4" : "max-h-0 opacity-0"}`}>
        <nav className="flex flex-col gap-4">
          {navLinks.map((link) => (
            <Link key={link.href} href={link.href} onClick={() => setIsOpen(false)} className="text-base font-medium px-2 hover:text-orange-500">
              {link.name}
            </Link>
          ))}
          <Link href="/login" className="mt-2 mx-2 text-center py-3 bg-linear-to-r from-orange-500 to-[#000080] text-white rounded-xl font-bold">
            Log In
          </Link>
        </nav>
      </div>
    </header>
  );
}