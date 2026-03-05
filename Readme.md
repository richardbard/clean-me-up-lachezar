# Clean Me Up - Email REST Service

A lightweight Spring Boot REST service that exposes a simple API for
sending emails via SMTP.

The service accepts a JSON request, validates the input, maps it to an
SMTP email model, and delegates the actual sending to an SMTP handler.

## Features

-   REST API for sending emails
-   Input validation using Jakarta Bean Validation
-   Centralized exception handling
-   Simple SMTP integration
-   Unit and API tests

------------------------------------------------------------------------

# API

## Send Email

**Endpoint**

POST /v1/email/send

**Request Body**

``` json
{
  "recipients": ["john@example.com"],
  "subject": "Hello",
  "body": "Test message"
}
```

### Validation Rules

Field        Rule
  ------------ --------------------------------
recipients   required, 1--50 values
subject      required, max 255 characters
body         optional, max 65000 characters

------------------------------------------------------------------------

## Successful Response

HTTP 200

``` json
{
  "isSuccess": true
}
```

------------------------------------------------------------------------

## Validation Error

HTTP 400

``` json
{
  "isSuccess": false,
  "errorMessage": "recipients: Please specify at least one recipient"
}
```

------------------------------------------------------------------------

## Internal Error

HTTP 500

``` json
{
  "isSuccess": false,
  "errorMessage": "Something Went Wrong!"
}
```

------------------------------------------------------------------------

# Project Structure

    clean-me-up
     ├── clean-me-up-support
     │     ├── SmtpEmail
     │     └── SmtpHandler
     │
     └── clean-me-up-rest
           ├── handler
           │     └── EmailHandler
           │
           ├── rest
           │     └── EmailRestController
           │
           ├── mapper
           │
           ├── config
           │
           └── exception
                 └── GlobalExceptionHandler

------------------------------------------------------------------------

# Running the Application

### Build

``` bash
mvn clean install
```

### Run

``` bash
mvn spring-boot:run
```

or run the generated jar:

``` bash
java -jar target/clean-me-up-rest.jar
```

------------------------------------------------------------------------

# Testing

Run unit and API tests:

``` bash
mvn test
```

Tests include:

-   Handler unit tests
-   Controller API tests
-   Validation error scenarios

------------------------------------------------------------------------

# Configuration

SMTP credentials are configured via application properties:

``` properties
email.username=your-smtp-user
email.password=your-smtp-password
```

------------------------------------------------------------------------

# Technologies

-   Java 21
-   Spring Boot 4
-   Spring WebMVC
-   Jakarta Validation
-   MapStruct
-   Lombok
-   JUnit 5
-   Mockito
