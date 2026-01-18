import { BlogPost } from '@/types/blogPost';
import { notFound } from 'next/navigation';

async function getBlogPost(slug: string): Promise<BlogPost | null> {
  try {
    const res = await fetch(`${process.env.API_URL}/api/blog/${slug}`, {
      next: { revalidate: 60 }, // revalidate every 60s, or use cache: 'no-store'
    });
    if (!res.ok) return null;
    return res.json();
  } catch {
    return null;
  }
}

export default async function BlogPostPage({ params }: { params: { slug: string } }) {
  const post = await getBlogPost(params.slug);

  if (!post) notFound();

  return (
    <article className="min-h-screen bg-gray-50 py-12">
      <div className="container mx-auto px-4 max-w-4xl">
        <header className="mb-12">
          <h1 className="text-5xl md:text-6xl font-bold text-gray-900 mb-6">
            {post.title}
          </h1>
          <div className="flex flex-wrap items-center gap-4 text-gray-600">
            <div className="flex items-center gap-2">
              <div className="w-10 h-10 bg-linear-to-r from-purple-500 to-blue-500 rounded-full flex items-center justify-center text-white font-bold">
                {post.authorName?.charAt(0) || 'B'}
              </div>
              {post.authorName && <span>{post.authorName}</span>}
            </div>
            {post.publishedAt && (
              <>
                <span>•</span>
                <time>
                  {new Date(post.publishedAt).toLocaleDateString('en-US', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                  })}
                </time>
              </>
            )}
          </div>
        </header>

        <div className="prose prose-lg max-w-none">
          {/* In production, use a markdown renderer instead of dangerouslySetInnerHTML */}
          <div dangerouslySetInnerHTML={{ __html: post.content }} />
        </div>
      </div>
    </article>
  );
}
