import Link from 'next/link';

export default function Footer() {
  return (
    <footer className="bg-blue-500 text-white mt-12">
      <div className="container mx-auto px-6 py-10 grid grid-cols-1 md:grid-cols-3 gap-8">
        
        {/* Quick Links */}
        <div>
          <h3 className="text-lg font-semibold mb-4">Quick Links</h3>
          <ul className="space-y-2">
            <li><Link href="/courses" className="hover:text-orange-300">Courses</Link></li>
            <li><Link href="/research" className="hover:text-orange-300">Research</Link></li>
            <li><Link href="/data" className="hover:text-orange-300">Data Analysis</Link></li>
            <li><Link href="/software" className="hover:text-orange-300">Software Development</Link></li>
            <li><Link href="/blog" className="hover:text-orange-300">Blog</Link></li>
            <li><Link href="/contact" className="hover:text-orange-300">Contact</Link></li>
          </ul>
        </div>

        {/* Contact Info */}
        <div>
          <h3 className="text-lg font-semibold mb-4">Contact Us</h3>
          <p>View Park Towers, 9th Floor, Nairobi, Kenya</p>
          <p>+254 702 588644 / +254(0)208000289</p>
          <p>info@bluestron.co.ke</p>
          <p>www.bluestron.co.ke</p>
        </div>

        {/* Newsletter & Social */}
        <div>
          <h3 className="text-lg font-semibold mb-4">Stay Connected</h3>
          <form className="flex space-x-2">
            <input
              type="email"
              placeholder="Your email"
              className="p-2 rounded text-gray-900 grow"
            />
            <button className="bg-orange-500 px-4 py-2 rounded-lg font-semibold">Subscribe</button>
          </form>
          <div className="flex space-x-4 mt-4">
            <Link href="#" className="hover:text-orange-300">Facebook</Link>
            <Link href="#" className="hover:text-orange-300">Twitter</Link>
            <Link href="#" className="hover:text-orange-300">LinkedIn</Link>
          </div>
        </div>
      </div>
      <div className="text-center py-4 bg-purple-500">
        &copy; {new Date().getFullYear()} Bluestron. All rights reserved.
      </div>
    </footer>
  );
}
