> curl -X POST http://localhost:8080/api/v1/employee -H 'Content-Type: application/json' -d '{"name":"Sergio","surname":"Rodriguez","birth":"1989-12-23","email":"sergio@mail.com"}' -w '\n' -i

> curl http://localhost:8080/api/v1/employee -w '\n' -i

> curl http://localhost:8080/api/v1/employee/1 -w '\n' -i

> curl -X DELETE http://localhost:8080/api/v1/employee/1 -w '\n' -i