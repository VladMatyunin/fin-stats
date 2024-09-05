# Project description
A high-performance RESTful service capable
of handling the rigorous demands of high-frequency trading systems. This service acts as a component in the company ABC trading infrastructure, managing and
analysing financial data in near real-time.

# Technical Description
- Uses in memory storage to store financial data
- Uses Segment Tree structure for fast statistics retrieval
- Has 2 endpoints: '/add_batch' to insert batch data, '/stats' to retrieve stats for specified symbol
- Built with Spring Boot

# Running locally
- Ensure Java SDK 21 is installed on your machine
- Run `./gradlew bootRun` in your terminal
- Test the API using the following command `curl 'localhost:8080/stats?symbol=APPL1&k=1'`. Should return a json with message 'Symbol not found'

# Hints
- Project uses Lombok for boilerplate replacements of getters/setters, ensure that Annotation Processing is enabled in Idea