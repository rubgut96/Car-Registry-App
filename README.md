
# Car Registry APP Backend

## Introduction

This straightforward backend is designed to manage the car registration process. Provides an API to create, update and retrieve car registration data. In addition, it has token-based web security authentication implemented.

## Requirements

- Java JDK 17 or higher

## Quick Start Guide

To get the Car Registry APP backend up and running, follow these simple steps:

- Open in IntelliJ: Start by opening the project in IntelliJ IDEA to load all necessary dependencies.

- Docker Compose Execution: Run the docker-compose.yml file to establish a MySQL database.

- Application Initialization: Finally, initiate the application. This will automatically generate the required tables in the database of the container.

## Usage Recommendations

After finishing the above steps, follow these recommendations to ensure smooth operation:

Swagger UI Access: Visit http://localhost:8080/swagger-ui/index.html to view the various endpoints and their descriptions. Please note that the IDs for cars and brands must be non-null.

Endpoint Usage: To begin using the endpoints, send a request to /users/signUp. This will create a user with the role ROL_CLIENT. Then, use a database management tool like DBeaver to change their role to ROL_VENDOR. With this change, you’ll have a user ready to utilize all the endpoints.
