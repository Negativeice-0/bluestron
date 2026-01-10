Spring boot is working well (but we will need to add a password to avoid using the security key spring generates as we have not yet built authentication), The problem is curl seems to be bloked (Sat Jan 10 07:08:47 EAT 2026
There was an unexpected error (type=Not Found, status=404).) -- lsetga@lsetga:~/Advance/ambrosia/bluestron$ curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "monitoring-evaluation",
    "name": "Monitoring & Evaluation",
    "description": "M&E courses",
    "status": "active"
  }'
lsetga@lsetga:~/Advance/ambrosia/bluestron$ curl http://localhost:8080/api/categories
lsetga@lsetga:~/Advance/ambrosia/bluestron$ , we cannot proceed until all that backend grunt work actually works -- right now there is no way to be sure i need to be sure, could be that cors is blocking browser request while spring boot security blocks curl, could we implement the frontend to test the backend with?