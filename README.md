# OutOfGas — Система учёта коммунальных услуг

Это проект для учёта коммунальных ресурсов, подачи показаний счётчиков, заявок и оплаты услуг.

---

## Локальный запуск

### 1. Стек

- Бэкенд: Kotlin + Spring Boot
- База данных: PostgreSQL
- Аутентификация: Firebase
- Фронтенд: HTML + JavaScript (в Docker-контейнере)
- Докеризация: PostgreSQL + Frontend


### 2. Скачай Firebase конфиг
Скачай firebase-config.json из Firebase Console и положи в:
src/main/resources/firebase-config.json

### 3. Подними базу и фронт через Docker
В корне проекта выполни:
```docker-compose up --build -d```

Это создаст и запустит:
PostgreSQL (localhost:5432)
Фронтенд (http://localhost:8081)

Запусти backend
```./gradlew bootRun```

### 4. Открой frontend
Открой в браузере:
http://localhost:8081
Фронтенд будет автоматически взаимодействовать с backend.

---

## Тестирование API
Тестирование производи в Postman, полный список маршрутов и примеров:
[Тесты API](docs/API_TESTS.md)

---

## Деплой на сервер
Инструкция по развертыванию проекта на сервере (JAR + Docker):
[Инструкция по деплою](docs/DEPLOY.md)

