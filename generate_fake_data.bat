@echo ohh
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"1\",\"name\":\"Karoline Herfurth\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"2\",\"name\":\"Til Schweiger\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"3\",\"name\":\"Dieter Hallervorden\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"4\",\"name\":\"Daniel Brühl\"}"
curl -X POST localhost:8080/users -H "Content-Type: application/json" -d "{\"id\":\"5\",\"name\":\"Sibel Kekilli\"}"

curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"1\",\"title\":\"Meeting 1\",\"from\":\"2019-05-20T14:00:00+02:00\",\"to\":\"2019-05-20T15:00:00+02:00\",\"userid\":\"3\"}"
curl -X POST localhost:8080/appointments -H "Content-Type: application/json" -d "{\"id\":\"2\",\"title\":\"Meeting 2\",\"from\":\"2019-05-20T17:00:00+02:00\",\"to\":\"2019-05-20T17:30:00+02:00\",\"userid\":\"3\"}"