# TaskFlow

TaskFlow — микросервисное приложение для управления задачами, уведомлениями и логированием событий. Проект создан в учебных и демонстрационных целях с целью отработки навыков проектирования распределённых систем и взаимодействия между сервисами.

## Архитектура

Проект состоит из трёх микросервисов:

- **TaskService** — основной сервис управления задачами, проектами и комментариями. Публикует события в Kafka и отправляет уведомления в RabbitMQ.
- **NotificationService** — обрабатывает сообщения из RabbitMQ, реализует поддержку Retry, DLQ и всех типов Exchange.
- **EventConsumerService** — читает события из Kafka и сохраняет логи в MongoDB.

## Технологии

- **Java 21**
- **Spring Boot**, **Spring Web**, **Spring Data JPA**, **Spring Validation**
- **PostgreSQL** + **Flyway** + **Liquibase**
- **MongoDB**
- **RabbitMQ** (Direct, Fanout, Topic, Headers Exchange + Retry + DLX)
- **Apache Kafka**
- **Testcontainers**, **JUnit 5**, **Mockito**
- **Docker** + **Docker Compose**

## Запуск проекта

### Требования

- Java 21+
- Gradle 8+
- Docker и Docker Compose

### В вашем IDE (например, IntelliJ IDEA) откройте соответствующий модуль/проект и запустите класс с методом main:

#### TaskService:
- ``` docker-compose up --build ```
- Класс запуска — TaskFlowApplication
- Запускает сервис управления задачами и публикации событий.

#### NotificationService:
- ``` docker-compose up --build ```
- Класс запуска — NotificationServiceApplication
- Запускает сервис обработки уведомлений и сообщений из RabbitMQ.

#### EventConsumerService:
- ``` docker-compose up --build ```
- Класс запуска — EventConsumerServiceApplication
- Запускает сервис потребления событий из Kafka и сохранения логов в MongoDB.

## После запуска:

- **TaskService**: [http://localhost:8080](http://localhost:8080)
- **NotificationService**: [http://localhost:8081](http://localhost:8081)
- **Kafka**: [http://localhost:9092](http://localhost:9092)
- **RabbitMQ**: [http://localhost:15672](http://localhost:15672) (логин/пароль: guest/guest)
- **MongoDB**: [http://localhost:27017](http://localhost:27017)

## Взаимодействие между сервисами:

- `TaskService → Kafka`: отправка событий `task_created`, `task_updated`, `project_deleted` и т.д.
- `EventConsumerService ← Kafka`: приём событий, запись логов в MongoDB
- `TaskService → RabbitMQ → NotificationService`: отправка и обработка уведомлений с поддержкой `ACK/NACK`, `Retry` и `DLX`
