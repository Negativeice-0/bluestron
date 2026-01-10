Once we are done i will need a report for; what was the issue, how we troubleshooted and how we fixed it.
it appears there are no mappings:<lsetga@lsetga:~/Advance/ambrosia/bluestron$ curl http://localhost:8080/actuator/mappings>

But swagger says other wise:"http://localhost:8080/swagger-ui/index.html"
</v3/api-docs
Explore
OpenAPI definition
 v0 
OAS 3.0
/v3/api-docs
Servers

http://localhost:8080 - Generated server url
Trainers


GET
/api/trainers
List trainers with optional filter


POST
/api/trainers
Create a trainer


GET
/api/trainers/{slug}
Get trainer by slug

Testimonials


GET
/api/testimonials
List testimonials with optional filter


POST
/api/testimonials
Create a testimonial

Service Offerings


GET
/api/service-offerings
List services with optional filter


POST
/api/service-offerings
Create a service offering


GET
/api/service-offerings/{slug}
Get service by slug

Service Enquiries


POST
/api/service-enquiries
Submit a service enquiry

Registrations


POST
/api/registrations
Register for a course/cohort

Featured Content


GET
/api/featured-content
List featured content with optional filters


POST
/api/featured-content
Create featured content

Courses


GET
/api/courses
List courses with optional filters


POST
/api/courses
Create a course


GET
/api/courses/{slug}
Get course by slug

Course Instances


GET
/api/course-instances
List course instances with optional filters


POST
/api/course-instances
Create a course instance

Blog Posts


GET
/api/blog-posts
List blog posts with optional filter


POST
/api/blog-posts
Create a blog post


GET
/api/blog-posts/{slug}
Get blog post by slug

status-controller


GET
/api/status

Homepage


GET
/api/homepage
Homepage composite: featured items, trainers, testimonials


Schemas
TrainerDTO
Trainer
TestimonialDTO
Testimonial
ServiceOfferingDTO
ServiceOffering
ServiceEnquiryDTO
ServiceEnquiry
RegistrationDTO
Course
CourseCategory
CourseInstance
Registration
Venue
FeaturedContentDTO
FeaturedContent
CourseDTO
CourseInstanceDTO
BlogPostDTO
BlogPost>

## Error Type
Console TypeError

## Error Message
Failed to fetch


    at CategoriesPage.useEffect (src/app/categories/page.tsx:8:5)

## Code Frame
   6 |
   7 |   useEffect(() => {
>  8 |     fetch('http://localhost:8080/api/categories')
     |     ^
   9 |       .then(res => res.json())
  10 |       .then(setCategories)
  11 |       .catch(err => console.error(err))

Next.js version: 16.1.1 (Turbopack)