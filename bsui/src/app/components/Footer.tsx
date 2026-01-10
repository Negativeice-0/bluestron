export default function Footer() {
  return (
    <footer className="px-4 py-8 bg-softwhite border-t border-navy/10 text-sm">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <h4 className="font-semibold">Contact</h4>
          <p className="text-navy/70">View Park Towers, 9th Floor, Nairobi</p>
          <p className="text-navy/70">+254 702 588644</p>
          <p className="text-navy/70">info@bluestron.co.ke</p>
        </div>
        <div>
          <h4 className="font-semibold">Quick links</h4>
          <ul className="text-navy/70 space-y-1">
            <li><a href="/courses" className="hover:text-orange">Courses</a></li>
            <li><a href="/services" className="hover:text-orange">Services</a></li>
            <li><a href="/blog" className="hover:text-orange">Blog</a></li>
            <li><a href="/contact" className="hover:text-orange">Contact</a></li>
          </ul>
        </div>
        <div>
          <h4 className="font-semibold">Newsletter</h4>
          <p className="text-navy/70">Sign up to receive updates on upcoming courses.</p>
          <button className="mt-2 px-3 py-2 bg-orange text-white rounded-md">Subscribe</button>
        </div>
      </div>
      <div className="mt-6 text-navy/60">© Bluestron</div>
    </footer>
  )
}
