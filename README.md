## agency

#### Install

Compile and run service with

```
mvn clean compile exec:java
```

Server is listening on port 8080

#### Use service

To enter new user

```
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"1\",\"name\":\"Karoline Herfurth\"}"
```

To enter new appointment

```
curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"2\",\"title\":\"Meeting 2\",\"from\":\"2019-05-20T17:00:00+02:00\",\"to\":\"2019-05-20T17:30:00+02:00\",\"users\":[\"1\"]}"

```

#### Available end points

##### User

* POST /users
* GET /users/:id
* PATCH /users
* DELETE /users/:id

* GET /users

##### Appointment

* POST /appointments
* GET /appointments/:id
* PATCH /appointments
* DELETE /appointments/:id

* GET /appointments
* GET /appointments/user/:id
* DELETE /appointments/user/:id


