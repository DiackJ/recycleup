# Customer Feedback API

## Table of Contents:

- [Project description](#description)

- [Why I built it](#why-i-built-it)

- [How to run the project](#how-to-run)

- [Tech stack and features](#stack--features)

- [What I learned by building this project](#what-i-learned)

## Description

This customer feedback API was part of a coding challenge I did that was offered by HyperSkill. Its purpose as a product
is to simplify the search process for users by helping them sort through customer reviews using filtering and pagination. As
a coding challenge, its purpose is to familiarize yourself with common backend practices and tools such as search filtering, 
pagination, and using a MongoDB database. 

## Why I Built It:

I chose to do this project to learn the basics of MongoDB as a NoSQL database as well as to learn about the search filtering
process and how pagination works to provide users with a better viewing experience. 

## How to Run:

**1. Clone the repository**
>git clone https://github.com/DiackJ/customerFeedback.git
> 
> cd customerFeedback

**2. Create a MongoDB database locally**
- Create a MongoDB database locally or with a tool like MongoDB Atlas (e.g. *feedback_db*)
- Within that database create a collection called *reviews*

**3. Configure application properties**
- Navigate to the following file and replace the database name and uri with the name of your database name and uri connection string.
  >/src/main/resources/application.properties
    > 
          - example:
                spring.data.mongodb.database=your_database_name
     
                spring.data.mongodb.uri=your_uri_connection
- Navigate to the following file and please comment out or remove the following line of code:
- file: 
>/src/main/java/com/CustomerFeedbackApplication.java
- code to comment out or remove so the correct database runs:

          static{
                EnvLoader.loadEnv();
          }

**4. Build & Run the application**
- run the bash command:
   > ./mvnw.cmd spring-boot:run

**5. Test the application**
- Use any API tool of your choice (e.g. Postman) to test the endpoints located in:
   > /src/main/java/com/FeedbackController.java
- The base URL is: 
   > http://localhost:8080

## Stack & Features:

**Tech Stack**
- Java
- Spring Boot 
- MongoDB (with Atlas)

**Features**

This small microservice inspired API allows users to:
-   Add their own review of a specific product.

-   Set filter options to view other product reviews by vendor, product, rating, or customer.

-   Add page constraints to limit the number of reviews seen on each page for a better experience. 


## What I Learned:

- Practiced the basics of setting up a **MongoDB** database using MongoDB Atlas and connecting it to my application.
- Used Spring Boot's MongoDB packages such as **MongoTemplate** and **Query** to apply filtering and pagination. 
- Learned the basics of **search filtering**, including how to apply optional filter criteria for the user to view customer reviews
  based on certain specifications like product, vendor, or rating.
- Learned the basics of **pagination**, including how to set a default number of elements per page and how to take in user specifications
  to let the user define how many elements they want to see per page. 
- Experience using an **.env file** to hide sensitive database information for production readiness. 