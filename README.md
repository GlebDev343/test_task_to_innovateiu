# Document Manager

A simple Java-based document management system with in-memory storage. This project includes the `DocumentManager` class for managing documents and JUnit 5 tests to verify its functionality.

## Prerequisites

- **Java**: Version 17 (`java -version` to check)
- **Maven**: For building and running tests (`mvn -version` to check)

If you don't have Maven, download it from [maven.apache.org](https://maven.apache.org/) and follow the installation guide.

## Quick Start

### Clone the Repository

```bash
git clone https://github.com/GlebDev343/test_task_to_innovateiu.git
cd test_task_to_innovateiu
```

### Run the Tests

```bash
mvn test
```

That's it! Maven will handle everything:

- Download dependencies (Lombok, JUnit 5).
- Compile the code with Java 17.
- Execute the tests and display the results.

## Project Structure

```
src/
├── main/java/com/example/DocumentManager.java    # Core implementation
├── test/java/com/example/DocumentManagerTest.java # Unit tests
pom.xml                                           # Maven configuration file
```

## Notes

- The project uses **Lombok** for boilerplate reduction and **JUnit 5** for testing.
- All dependencies are managed by Maven, so no manual setup is needed.

## Troubleshooting

- **"mvn: command not found"**: Install Maven or add it to your `PATH`.
- **Java version error**: Ensure Java 17 is installed (`java -version`).

