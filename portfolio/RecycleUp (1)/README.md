# RecycleUp 

## Table of Contents:

- [Project description](#description)

- [Why I built it](#why-i-built-it)

- [How to run the project locally](#how-to-run)

- [Tech stack and project features](#stack--features)

- [What I learned through building the project](#what-i-learned)

- [Next steps for further development](#next-steps-)

## Description:

RecycleUp is a platform designed to encourage the next generation to get into recycling in a fun and engaging way. 
Families, friends, or even school classes can sign up and start tracking their recycling to help take care of our planet!

## Why I Built It:

One day I heard something that disappointed me more than shocked me: 
>"By the year 2050, there will be more plastic in the ocean than marine life." 

So that got me thinking - "how can we encourage the next generation to recycle more?"...By making it **fun**!

And this became the foundation of this project: an engaging platform designed to get the next generation excited
about their recycling efforts through goal setting, progress updates, and a reward system. And to make it even more engaging,
a little bit of playful competition via an in-house leader board.

## How To Run:

**1. Clone the repository**
>git clone https://github.com.DiackJ/recycleup-project.git
>
> cd RecycleUp

**2. Set up database**
- Create MySQL database named *recycleup_db*
- Run the SQL schema file located in
>/src/main/resources/schema.sql

**3. Configure application properties**
- Go to the file
>/src/main/resources/application.properties
  - fill in your database credentials:

          spring.datasource.url=jdbc:mysql:localhost:3306/recycleup_db
    
          spring.datasource.username=your_username*
    
          spring.datasource.password=your_password*
    
          jwt.secret=your_secret_key* (256 bytes or more)
  - please remove or comment out the following block in 
    >/src/main/java/com/RecycleUpApplication.java
  
      -  **block to comment out:**
   
              static {
     
                  EnvLoader loader = new EnvLoader(); //loads system properties from env at start of application
    
              }
- **4. Build & Run the application**
  - run the bash command:
    > ./mvnw spring-boot:run

-**5. Test the API**

-   Use postman or any other API tool to test the endpoints located in
    > /src/main/java/com.springboot.RecycleUp/Controller.java
    - The base URL is: 
    > http://localhost:8080 

-**6. (Optional) Run tests** 
 - run the bash command:
    > ./mvnw test


## Stack & Features:
**Tech Stack:**
- Java
- Spring Boot
- MySQL
- JUnit (testing)

**Why This Stack:**

I chose this stack for its scalability and modularity. Spring Boot makes for a clean REST API and Java makes for a robust
and stable backend. Spring Boot also allows for seamless database integration and the ability to upgrade and add on in the 
future with microservices. And MySQL is a sturdy database good for its relational mapping and seamless Spring Boot integration 
and its performance can be easily enhanced by adding caching later on. 

## What I Learned:

-    Improved my understanding of common Spring Boot annotations and how they work together with Java to create scalable APIs.
-    Integrated production-ready authentication using **Spring Security** and **JWT tokens** to ensure
     secure access to protected endpoints.
-    Wrote unit tests using **JUnit** to improve code reliability and catch bugs before end-to-end testing.
-    Implemented error handling in controllers to help guide users through invalid or missing inputs.

## Next Steps: 

**Ideas For Future Features/Additions:**

-    **Caching** with Redis to make the program more scalable, fast and increase database performance.
-    **more user analytics:**
      - Group-level analytics to calculate goals, progress, points and recycling efforts across the whole account.
      - Monthly/Yearly summaries to show individual users their recycling progress.
-    **Refactor tests** to run more seamlessly with the test database  
-    **Role-based authorization** to limit account and profile deletion to the account's primary profile only. 