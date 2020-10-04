# Test QA project (Serenity JUnit RestAssured)

## Description

The project use libraries:
- Serenity, as wrapper for generate human readable reports with living documentation approach
- Junit, as runner of automation tests
- RestAssured was used for validation REST services
- Mock-server for mocks HTTP requests
- Lombock for ease of writing of bindings
- Assertj for assertion

## Versions

Maven version: Apache Maven 3.3.9

Java version: 1.8.0

## Get the code

Git:

    git clone https://github.com/igTkachov/serenity-junit-restassured.git
    cd serenity-junit-restassured

## Use Maven to run tests
Automation tests contain 2 types of tests:
1. With 2 layers of abstraction (test steps and steps implementation)
   To run it locally you should execute command:

   mvn clean verify -Dit.test=SortPostSerenityIT

2. Without any layers of abstraction. It's SortPostIT class
To run it locally you should execute command:

     mvn clean verify -Dit.test=SortPostIT

## Viewing the reports

This will produce a Serenity test report in the `target/site/serenity` directory (open `index.html`)
Or find in console link like below and open it
[INFO] SERENITY REPORTS
[INFO]   - Full Report: file:///Users/it/serenity-junit-restassured/target/site/serenity/index.html

## Test cases placed in the :
/src/test/java
