import { BlogPost } from '@/types/blogPost';
import Image from 'next/image';
import Link from 'next/link';

async function getBlogPosts(): Promise<BlogPost[]> {
  const res = await fetch(`${process.env.API_URL}/api/blog`, {
    next: { revalidate: 60 }, // revalidate every 60s, or use cache: 'no-store'
  });
  if (!res.ok) throw new Error('Failed to fetch blog posts');
  return res.json();
}

export default async function BlogPage() {
  const posts = await getBlogPosts();

  return (
    <div className="container mx-auto px-4 py-12">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-5xl font-bold text-purple-500 mb-4">
          Bluestron Blog
        </h1>
        <p className="text-xl text-gray-600 mb-12">
          Insights, tutorials, and stories from the frontier of scientific computing.
        </p>

        <div className="space-y-12">
          {posts.map((post) => (
            <article
              key={post.id}
              className="bg-white rounded-2xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
            >
              {post.coverImagePath && (
                <div className="relative h-64 md:h-80">
                  <Image
                    src={`${process.env.API_URL}/api/media/${post.coverImagePath}`}
                    alt={post.title}
                    fill
                    className="object-cover"
                    sizes="(max-width: 768px) 100vw, 800px"
                  />
                </div>
              )}
              <div className="p-8">
                <div className="flex items-center gap-4 mb-4">
                  {post.publishedAt && (
                    <span className="px-3 py-1 bg-blue-100 text-blue-500 rounded-full text-sm">
                      {new Date(post.publishedAt).toLocaleDateString()}
                    </span>
                  )}
                  {post.authorName && (
                    <span className="text-gray-500">by {post.authorName}</span>
                  )}
                </div>

                <h2 className="text-3xl font-bold text-gray-900 mb-4">
                  {post.title}
                </h2>

                {post.excerpt && (
                  <p className="text-gray-600 mb-6 text-lg">{post.excerpt}</p>
                )}

                <Link
                  href={`/blog/${post.slug}`}
                  className="inline-flex items-center text-orange-500 font-semibold hover:text-orange-600"
                >
                  Read full article
                  <svg
                    className="w-5 h-5 ml-2"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M14 5l7 7m0 0l-7 7m7-7H3"
                    />
                  </svg>
                </Link>
              </div>
            </article>
          ))}
        </div>
      </div>
    </div>
  );
}
