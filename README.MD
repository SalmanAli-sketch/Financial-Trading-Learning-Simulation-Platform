# Stock Portfolio Management System

> A Java backend project demonstrating multithreaded, transaction-safe database operations for portfolio/trading data processing, built with PostgreSQL, JDBC, and the DAO pattern.

*(Update the title/description above if this doesn't match your project's actual name or purpose.)*

## Overview

This project is a backend system built in core Java that simulates concurrent processing of financial/portfolio data, backed by a PostgreSQL database. It follows a layered architecture (DAO + Service layer) and uses Java's concurrency utilities to safely process data across multiple threads while maintaining database transaction integrity.

## Features

- Multithreaded concurrent data processing using `ExecutorService` and `BlockingQueue`
- Thread-safe operations using `ReentrantLock`
- Database transactions with commit/rollback support
- DAO pattern for clean separation of persistence logic
- Service layer architecture for business logic
- Robust exception handling and logging
- File handling for input/output data
- Report generation

## Tech Stack

- **Language:** Java
- **Database:** PostgreSQL
- **Data Access:** JDBC
- **Concurrency:** `ExecutorService`, `BlockingQueue`, `ReentrantLock`
- **Build Tool:** *(e.g., Maven / Gradle — update as applicable)*

## Architecture

```
src/
├── model/          # Domain/entity classes
├── dao/            # Data Access Objects (JDBC + SQL)
├── service/        # Business/service layer
├── db/             # Database connection handling
├── util/           # Utility/helper classes
├── report/         # Report generation logic
└── Main.java       # Application entry point
```

*(Adjust this to match your actual package/folder structure.)*

## Getting Started

### Prerequisites

- Java JDK 17+ (or your project's version)
- PostgreSQL installed and running
- Maven or Gradle (if applicable)

### Database Configuration

⚠️ **Do not hardcode database credentials in source code.** Instead of storing the password directly in `DatabaseConnection.java`, use one of the following approaches:

**Option 1: Environment variables**

```java
private static final String URL = System.getenv("DB_URL");
private static final String USER = System.getenv("DB_USER");
private static final String PASSWORD = System.getenv("DB_PASSWORD");
```

Set them before running:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/your_db
export DB_USER=your_username
export DB_PASSWORD=your_password
```

**Option 2: External config file (excluded from Git)**

Create a `config.properties` file (and add it to `.gitignore`):

```properties
db.url=jdbc:postgresql://localhost:5432/your_db
db.user=your_username
db.password=your_password
```

Load it in `DatabaseConnection.java` using `Properties` + `FileInputStream`, and never commit this file.

Add to `.gitignore`:

```
config.properties
*.env
```

> If a real password has already been committed to Git history, rotate/change that PostgreSQL password and consider scrubbing it from history (e.g., with `git filter-repo` or BFG Repo-Cleaner) before making the repository public.

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/<your-username>/<your-repo>.git
   ```
2. Create the PostgreSQL database and required tables
3. Configure database credentials as described above
4. Build and run the project
   ```bash
   # Maven example
   mvn clean install
   mvn exec:java
   ```

## Key Learning Outcomes

This project demonstrates practical understanding of:

- Object-Oriented Programming
- Java Collections
- Exception Handling
- File Handling
- JDBC
- PostgreSQL
- SQL
- DAO pattern
- Service layer architecture
- Multithreading
- ExecutorService
- BlockingQueue
- ReentrantLock
- Thread safety
- Database transactions (commit and rollback)
- Concurrent data processing
- Logging
- Report generation

## Future Improvements

Possible future enhancements:

- REST API using Spring Boot
- JWT authentication
- Web-based frontend
- Real-time market data API
- Redis caching
- Docker deployment
- Microservices architecture
- Kafka-based event processing

## Author

**Salman Ali**
Java Backend Developer | B.Tech CSE