export default function Footer() {
  return (
    <footer className="px-4 py-12 bg-[#000080] text-white relative z-0">
      <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-6 text-center md:text-left">
        {/* Contact */}
        <div>
          <h4 className="font-semibold">Contact</h4>
          <p>View Park Towers, 9th Floor, Nairobi</p>
          <p>+254 702 588644</p>
          <p>info@bluestron.co.ke</p>
        </div>

        {/* Quick links */}
        <div>
          <h4 className="font-semibold">Quick links</h4>
          <ul className="space-y-1">
            <li><a href="/courses" className="hover:text-orange-300">Courses</a></li>
            <li><a href="/services" className="hover:text-orange-300">Services</a></li>
            <li><a href="/blog" className="hover:text-orange-300">Blog</a></li>
            <li><a href="/contact" className="hover:text-orange-300">Contact</a></li>
          </ul>
        </div>

        {/* Newsletter centered */}
        <div className="flex flex-col items-center md:items-start">
          <h4 className="font-semibold">Newsletter</h4>
          <p className="mb-2">Sign up to receive updates on upcoming courses.</p>
          <button className="px-4 py-2 bg-white text-[#000080] rounded-md hover:bg-gray-100 transition">
            Subscribe
          </button>
        </div>
      </div>
      <div className="mt-6 text-center opacity-80">© Bluestron</div>
    </footer>
  );
}
