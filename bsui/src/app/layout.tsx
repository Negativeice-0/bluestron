import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";
import Navbar from "./components/Navbar";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
  display: 'swap',
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
  display: 'swap',
});

export const metadata: Metadata = {
  title: {
    template: "%s | Bluestron",
    default: "Bluestron | Professional Services", 
  },
  description: "High-quality courses and services by Bluestron.",
  icons: {
    icon: "/bs-clean.png",
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className={`${geistSans.variable} ${geistMono.variable}`}>
      <body className="antialiased min-h-screen flex flex-col bg-white text-[#000080]">
        <Navbar />

        {/* Main Content Area */}
        <main className="grow">
          {children}
        </main>

        {/* Standardized Footer */}
        <footer className="px-4 py-8 border-t border-navy/10 bg-gray-50/50">
          <div className="max-w-7xl mx-auto flex flex-col md:flex-row justify-between items-center gap-4 text-sm text-navy/60">
            <div className="flex items-center gap-2 font-semibold">
              <span className="text-lime-500">-</span> Bluestron
            </div>
            <p>© {new Date().getFullYear()} Bluestron. All rights reserved.</p>
            <div className="flex gap-4">
              <Link href="/privacy" className="hover:underline">Privacy</Link>
              <Link href="/terms" className="hover:underline">Terms</Link>
            </div>
          </div>
        </footer>
      </body>
    </html>
  );
}