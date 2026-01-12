import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import Footer from "./components/Footer";
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
    icon: "/logo.svg",
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

        <main className="grow">{children}</main>

        {/* Site-wide CTA strip above footer */}
        <div className="max-w-7xl mx-auto -mb-12 relative z-10">
          <div className="flex flex-col md:flex-row justify-center gap-4 px-6 py-6 bg-white/20 backdrop-blur-md rounded-xl shadow-lg">
            <a href="/courses" className="px-5 py-2 bg-white text-[#000080] rounded-md font-semibold hover:bg-gray-100 transition" >
              Register Now
            </a>
            <a href="/contact" className="px-5 py-2 bg-white text-orange-500 rounded-md font-semibold hover:bg-gray-100 transition" >
              Enquire
            </a>
            <a href="/services" className="px-5 py-2 bg-white text-lime-500 rounded-md font-semibold hover:bg-gray-100 transition" >
              Learn More
            </a>
          </div>
        </div>

        <Footer />
      </body>
    </html>
  );
}