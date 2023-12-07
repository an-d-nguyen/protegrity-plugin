# Neo4j Protegrity Plugin

This project involves a Maven-based Java application designed to integrate Neo4j with Protegrity for dynamic data protection using User-Defined Functions (UDFs).

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven
- Access to ApplicationProtectorJava.jar from Protegrity

## Setup and Configuration

### Update Protegrity JAR Path
1. Locate the `ApplicationProtectorJava.jar` file on your machine.
2. Update the `<systemPath>` in the `pom.xml` to reflect the correct path to `ApplicationProtectorJava.jar`.

    ```xml
    <systemPath>/path/to/ApplicationProtectorJava.jar</systemPath>
    ```

### Configure Artifact and Version
In the `pom.xml`, modify the `<artifactId>` and `<version>` tags to name the output file according to your project's naming conventions.

    ```xml
    <artifactId>YourArtifactId</artifactId>
    <version>YourVersion</version>
    ```

## Compilation

To compile the project, navigate to the project's root directory and run:

```bash
mvn package
```

Or to clean and compile:

```bash
mvn clean package
```

The output JAR file will be located in the `target` directory with the name you set in the `pom.xml`.

## Modifying the Code

To add more UDFs or modify existing ones, edit the `ProtegrityPlugin.java` file located in `src/main/java/com/neo4j/ps/protegrity`.

- Refer to the existing UDF templates in `ProtegrityPlugin.java` for guidance on structure and implementation.

## Adding New UDFs

- Define a new method in `ProtegrityPlugin.java` with the `@UserFunction` annotation.
- Set the appropriate `@Description` and method signature.
- Use existing UDFs as a template for implementing new functionality.

## Further Notes

- Ensure all dependencies and plugins in the `pom.xml` are correctly configured.
- Regularly update your JDK and Maven to their latest versions for optimal performance and security.
- Consult Neo4j and Protegrity documentation for specific configuration and deployment instructions.
