
# **Neo4j Protegrity Plugin Integration Guide**

## **Introduction**

This document provides a detailed guide on how to integrate Neo4j with Protegrity using a Maven-based Java application for dynamic data protection via User-Defined Functions (UDFs).

### **Prerequisites:**
- Java Development Kit (JDK) 17 or higher
- Apache Maven
- Access to ApplicationProtectorJava.jar from Protegrity

---

## **1. Installing Prerequisites**

### **A. Installing Java Development Kit (JDK) 17 or Higher**

1. **Install JDK 17:** Open a terminal and run the following command:

   ```bash
   brew install openjdk@17
   ```

 

2. **Link JDK 17:** After installation, link JDK 17 to Homebrew using the command:

   ```bash
   brew link --force --overwrite openjdk@17
   ```

   **Screenshot:** Show the terminal with the successful linking message.

3. **Verify Installation:** Type `java -version` in the terminal to display the installed Java version.

   **Screenshot:** Capture the terminal showing the Java version.

### **B. Installing Apache Maven**

1. **Install Maven:** In the terminal, run:

   ```bash
   brew install maven
   ```

   **Screenshot:** Capture the terminal showing the Homebrew command and its output for Maven installation.

2. **Verify Installation:** Check if Maven is successfully installed by typing `mvn -v` in the terminal.

   **Screenshot:** Show the terminal with the Maven version.

---

## **2. Setting Up and Configuring the Project**

### **A. Accessing ApplicationProtectorJava.jar**

1. **Obtain the Jar:** Ensure you have access to `ApplicationProtectorJava.jar` from Protegrity. 

   **Note:** Provide details or contact information on how to obtain this JAR file.

### **B. Updating Protegrity JAR Path**

1. **Locate the Jar:** Find the `ApplicationProtectorJava.jar` on your machine.

2. **Update pom.xml:**
   - Open the `pom.xml` file of your project.
   - Update the `<systemPath>` element to reflect the path to `ApplicationProtectorJava.jar`.

     ```xml
     <systemPath>/path/to/ApplicationProtectorJava.jar</systemPath>
     ```

   **Screenshot:** Show the `pom.xml` before and after the path update.

### **C. Configuring Artifact and Version**

1. **Modify pom.xml:**
   - In the `pom.xml`, modify the `<artifactId>` and `<version>` tags to name the output file according to your project's naming conventions.

     ```xml
     <artifactId>YourArtifactId</artifactId>
     <version>YourVersion</version>
     ```

   **Screenshot:** Highlight the sections in `pom.xml` where changes are made.

---

## **3. Compiling the Project**

1. **Compile the Project:**
   - Navigate to the project's root directory.
   - Run `mvn package` to compile the project.

   **Screenshot:** Capture the command line showing the successful compilation.

2. **Clean and Compile (Optional):**
   - Run `mvn clean package` to clean the project before compilation.

   **Screenshot:** Show the process and outcome of the clean and compile commands.

3. **Locate the Output JAR:**
   - The output JAR file will be in the `target` directory.

   **Screenshot:** Capture the target directory showing the newly created JAR file.

---

## **4. Modifying the Code**

### **A. Editing the ProtegrityPlugin.java File**

1. **Access the File:**
   - Navigate to `src/main/java/com/neo4j/ps/protegrity` and open `ProtegrityPlugin.java`.

   **Screenshot:** Show the file path and the opened file in the editor.

2. **Modify or Add UDFs:**
   - Refer to existing UDF templates in the file for guidance on structure and implementation.

   **Screenshot:** Capture before and after snapshots of any code changes.

### **B. Adding New UDFs**

1. **Define New Methods:**
   - In `ProtegrityPlugin.java`, define new methods with the `@UserFunction` annotation.
   - Set the appropriate `@Description` and method signature.

   **Screenshot:** Show the newly added methods with annotations.

---

## **5. Further Recommendations**

- Ensure all dependencies and plugins in `pom.xml` are correctly configured.
- Regularly update JDK and Maven for optimal performance and security.
- Consult Neo4j and Protegrity documentation for specific instructions.

**Note:** Include links to relevant documentation or support resources.

---