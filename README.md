# Email Service

This is a sample Java / Maven / Spring Boot microservice that provides 
CRUD operations and allows you to send mails to all stored addresses.

## Getting Started

### Prerequisites

For building and running the application you need:
* Java JDK 19
* Maven 3.9.0
* Git

### Installing

You can clone the repository using this command:

```
git clone https://gitlab.com/rest7315807/email-service.git
```

To run application, please go to the file:
```
src/main/java/com/example/emailservice/EmailServiceApplication.java
```

And click the 'Run EmailServiceApplication' button.

### Dependencies
There are a number of third-party dependencies used in the project. 
Browse the Maven pom.xml file for details of libraries and versions used.

## How to use this application
This microservice provides endpoints that allow you to use its functionality
through HTTP methods. Running the application on your local server with port 
8080 set by default, the endpoints look like this:

POST method that allows you to save a new email address:
```
localhost:8080/emails
```
Usage example using JSON format:
```
{
    "address" : "example@example.com"
}
```

GET method that returns data about the email with the given id:
```
localhost:8080/emails/{emailId}
```

PUT method that allows you to update the email data with the given id:
```
localhost:8080/emails/{emailId}
```
Usage example using JSON format:
```
{
    "address" : "new_example@example.com"
}
```

GET method that returns data about all the emails:
```
localhost:8080/emails
```

DELETE method that allows you to delete the email with the given id:
```
localhost:8080/emails/{emailId}
```

POST method that allows you to send a mail to all stored addresses:
```
localhost:8080/emails/send
```
You can send a simple mail specifying its subject, content and 
optionally adding one or more attachments.

Usage example using JSON format (without attachments):
```
{
    "subject" : "Subject example",
    "content" : "Content example"
}
```

Usage example using JSON format (with attachments):
```
{
    "subject" : "Subject example",
    "content" : "Content example",
    "pathsToAttachments" : ["path/to/first/attachment", "path/to/second/attachment]
}
```

To send e-mails from your address, please go to the file:
```
src/main/resources/application.properties
```
And change these properties:
```
spring.mail.username=youremail@example.com
spring.mail.password=yourEmailPassword
```

You also need to go to the file:
```
src/main/java/com/example/emailservice/service/EmailService.java
```
And change this property:
```
helper.setFrom("youremail@example.com");
```

## Logging
Application logs are configured in the file:
```
src/main/resources/logback-spring.xml
```
This configuration creates a 'logs' folder with two files: 'general.log' 
and 'integration.log'. The 'general.log' contains general logs of the application.
The 'integration.log' file stores integration logs, i.e. related to incoming requests.

## Running the tests

To run application service methods tests, please go to the file: 

```
src/main/java/com/example/emailservice/service/EmailService.java
```
And click the 'Run EmailServiceTest' button.

## Authors

* **Mikołaj Kawczyński** - *Computer Science student* - [MKawcz](https://github.com/MKawcz)

