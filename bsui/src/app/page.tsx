export default function HomePage() {
  return (
    <section className="px-4 py-10">
      <h1 className="text-2xl md:text-3xl font-bold">Professional Training, Research & Software Development in Data, M&E, Management</h1>
      <p className="mt-3 text-navy/70">Minimalistic, mobile-first, managerial design.</p>
      <div className="mt-6 flex gap-3">
        <a className="px-4 py-2 bg-orange text-white rounded-md" href="/courses">Register now</a>
        <a className="px-4 py-2 border border-navy/20 rounded-md" href="/services">Learn more</a>
      </div>
    </section>
  )
}
