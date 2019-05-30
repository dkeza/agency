@echo off

echo Insert users
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"1\",\"name\":\"Karoline Herfurth\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"2\",\"name\":\"Til Schweiger\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"3\",\"name\":\"Dieter Hallervorden\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"4\",\"name\":\"Daniel Brühl\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"5\",\"name\":\"Sibel Kekilli\"}"

echo Insert appointments
curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"1\",\"title\":\"Meeting 1\",\"from\":\"2019-05-20T14:00:00+02:00\",\"to\":\"2019-05-20T15:00:00+02:00\",\"users\":[\"3\"]}"
curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"2\",\"title\":\"Meeting 2\",\"from\":\"2019-05-20T17:00:00+02:00\",\"to\":\"2019-05-20T17:30:00+02:00\",\"users\":[\"1\"]}"
curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"3\",\"title\":\"Meeting 3\",\"from\":\"2019-05-20T19:00:00+02:00\",\"to\":\"2019-05-20T20:00:00+02:00\",\"users\":[\"2\"]}"
curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"4\",\"title\":\"Meeting 4\",\"from\":\"2019-05-20T21:00:00+02:00\",\"to\":\"2019-05-20T22:30:00+02:00\",\"users\":[\"1\",\"3\"]}"

echo Read user
curl -X GET localhost:8080/users/1

echo Read appointment
curl -X GET localhost:8080/appointments/1

echo Read all users
curl -X GET localhost:8080/users

echo Read all apointments
curl -X GET localhost:8080/appointments

echo Read all apointments for user
curl -X GET localhost:8080/appointments/user/1

echo Update user
curl -X PATCH localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"1\",\"name\":\"Spiderman\"}"

echo Update appointment
curl -X PATCH localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"1\",\"title\":\"Meeting 2\",\"from\":\"2019-05-20T14:00:00+02:00\",\"to\":\"2019-05-20T15:00:00+02:00\",\"users\":[\"3\"]}"

echo Delete user
curl -X DELETE localhost:8080/users/1

echo Delete appointment
curl -X DELETE localhost:8080/appointments/1

echo Delete appointments for user
curl -X DELETE localhost:8080/appointments/user/2

echo Update user * empty json * error 1
curl -X PATCH localhost:8080/users -H "Content-Type: application/json"

echo Update user * empty id * error 2
curl -X PATCH localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"\",\"name\":\"Spiderman\"}"



