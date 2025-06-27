<a href="https://github.com/guilo23/Dev_Bank/actions/workflows/ci.yaml">
  <img src="https://github.com/guilo23/Dev_Bank/actions/workflows/ci.yaml/badge.svg" alt="CI - Dev_Bank"/>
</a>

<h1 align="center" style="font-size: 3em;">Dev_Bank</h1>

For the Portuguese version of this document, click [here](README.pt.md).

## 📋 Table of Contents

- [About the Project](#about-the-project)  
- [Features](#features)  
- [Technologies](#technologies)  
- [How to Run the Project (Preview)](#how-to-run-the-project-preview)  
- [Tests](#tests)  
- [Authors](#authors)  

## 💻 About the Project

Dev_Bank is a complete banking system that allows simulating common financial operations such as account creation, transfers, loans, payment order issuance, and transaction report generation.

> This project was developed exclusively for educational and learning purposes.

## ⚙️ Features

- Opening and managing bank accounts  
- Updating customer data  
- Making deposits and withdrawals  
- Transfers between accounts  
- Loan requests  
- Issuing payment orders  
- Generating financial transaction reports  
- Checking account balances and statements  

## 🛠️ Technologies

- [Git](https://git-scm.com/) — Source code version control  
- [Maven](https://maven.apache.org/) — Dependency and build manager  
- [Java](https://www.java.com) — Main programming language  
- [Spring Boot](https://spring.io/projects/spring-boot) — Backend development framework  
- [Spring Security](https://spring.io/projects/spring-security) — Security framework for authentication and authorization  
- [JUnit](https://junit.org/junit5/) — Java unit testing framework  
- [Mockito](https://site.mockito.org/) — Framework for creating mocks in Java tests  
- [Swagger](https://swagger.io/tools/swagger-ui/) — Tool for interactive API documentation  
- [Spotless](https://spotless.io/) — Plugin for automatic code formatting  
- [PMD](https://pmd.github.io/) — Static code analysis tool to detect bugs  
- [H2 Database](https://www.h2database.com/html/main.html) — In-memory database for local testing  
- [Docker](https://www.docker.com/) — Containerization for easy local preview  
- [PostgreSQL](https://www.postgresql.org/) — Relational database for production  
- [Next.js](https://nextjs.org/) — React framework for the frontend  

## 📦 How to Run the Project (Preview)

### Prerequisites

- [Docker](https://www.docker.com/get-started) installed on your machine  
- [Docker Compose](https://docs.docker.com/compose/install/) (usually comes with Docker)

### Steps to Run

1. Clone the repository:
    ```bash
    git clone https://github.com/guilo23/Dev_Bank
    ```

2. Run Docker Compose to start the backend, frontend, and database:
    ```bash
    docker-compose up
    ```

3. Wait for the containers to start. Then, access in your browser:
    - Frontend: `http://localhost:3000`
    - Swagger UI (backend API documentation): `http://localhost:8080/swagger-ui.html`

## 🧪 Tests

This project includes automated tests to ensure code quality and stability.

### Types of Tests Included

- Unit tests with JUnit and Mockito  
- ~~Integration tests to validate component workflows~~  

### How to Run Tests

To run all tests, execute the following command inside the `backend` directory:

```bash
mvn test
