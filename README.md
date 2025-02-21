Siopa Partner Details

Siopa Partner Details is a Spring Boot microservice responsible for managing stores and their owners through a set of RESTful APIs.
Prerequisites

    Java 17 or later
    Maven 3.6+
    PostgreSQL (or another supported relational database)

How to Run

    Clone the Repository

git clone https://github.com/your-repo/siopa-partner-details.git
cd siopa-partner-details

Configure the Database

Update the database connection settings in src/main/resources/application.properties (or application.yml). For example:

spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

Build the Application

Build the project using Maven:

mvn clean install

Start the Application

You can run the application using Maven:

mvn spring-boot:run

Or by running the generated jar:

java -jar target/siopa-partner-details-0.0.1-SNAPSHOT.jar

Access the APIs

Once started, the application is available at http://localhost:8080. Use an API client like Postman to test the endpoints.
