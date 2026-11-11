# Book Library API + PostgreSQL + Docker Compose

Учебный проект для контрольной точки: REST API на Java 17 / Spring Boot 3, PostgreSQL 16 и Docker Compose.  
Тема: библиотека книг. Сущность `Book` хранится в PostgreSQL.

## Требования

- Docker Desktop или Docker Engine + Docker Compose plugin
- `curl`

## Запуск

В терминале из корня проекта:

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f api db
```

API доступен на http://localhost:8080. Проверка связи с БД:

```bash
curl http://localhost:8080/health
```

Ожидаемый ответ: `{"api":"ok","database":"ok"}`.

## CRUD-проверка

### 1. Create — создать книгу

```bash
curl -i -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Война и мир","author":"Лев Толстой","isbn":"978-5-389-01234-5","year":1869,"genre":"Роман","available":true}'
```

Сохраните `id` из ответа (в чистой базе обычно `1`).

### 2. Read — получить список и одну книгу

```bash
curl -i http://localhost:8080/books
curl -i http://localhost:8080/books/1
```

### 3. Update — обновить книгу

```bash
curl -i -X PUT http://localhost:8080/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Война и мир","author":"Лев Толстой","isbn":"978-5-389-01234-5","year":1869,"genre":"Эпический роман","available":false}'
```

### 4. Delete — удалить книгу

```bash
curl -i -X DELETE http://localhost:8080/books/1
curl -i http://localhost:8080/books
```

Для проверки всех операций по порядку выполните POST, GET, PUT и DELETE. Если ID отличается от `1`, подставьте фактический ID из ответа POST.

## Проверка сохранения данных после перезапуска

Сначала создайте запись и убедитесь, что она видна через `GET /books`. Затем выполните:

```bash
docker compose down
docker compose up -d
docker compose ps
curl http://localhost:8080/books
```

Созданные записи должны остаться: Compose сохраняет каталог PostgreSQL в именованном томе `db_data`.

**Важно:** `docker compose down -v` удаляет том `db_data` и данные в нём. Не используйте `-v` при проверке сохранения.

## Полезные команды

```bash
docker compose ps
docker compose logs --tail=100 api
docker compose logs --tail=100 db
docker compose down
```

## Структура проекта

```text
.
├── src/main/java/com/library/bookapi/
│   ├── BookApiApplication.java
│   ├── controller/BookController.java
│   ├── model/Book.java
│   ├── model/BookInput.java
│   └── repository/BookRepository.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
└── README.md
```

## Архитектура

`curl/Postman -> API (Java / Spring Boot, порт 8080) -> PostgreSQL 16 (сервис db, порт 5432 внутри сети Compose)`

API подключается к БД по имени сервиса `db`, получает настройки через переменные окружения `SPRING_DATASOURCE_*`, запускается после healthcheck базы, а данные хранятся в `db_data`.
