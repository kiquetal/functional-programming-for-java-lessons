# Functional Programming Java Example

This is a simple Java application demonstrating how to configure Maven to create an executable JAR file.

## How to Build and Run

1.  **Build the project:**
    ```bash
    mvn clean package
    ```

2.  **Run the application:**
    ```bash
    java -jar target/functional-programming.jar
    ```

## POM Configuration Explained

To make the JAR executable via `java -jar`, we needed to add two specific configurations to the `pom.xml`:

### 1. `maven-jar-plugin` Configuration

We configured the `maven-jar-plugin` to modify the `MANIFEST.MF` file inside the JAR. Specifically, we set the `mainClass` attribute to our application's entry point (`com.functional.java.App`).

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-jar-plugin</artifactId>
  <version>3.4.1</version>
  <configuration>
    <archive>
      <manifest>
        <addClasspath>true</addClasspath>
        <mainClass>com.functional.java.App</mainClass>
      </manifest>
    </archive>
  </configuration>
</plugin>
```

### 2. `finalName` Configuration

We set the `<finalName>` to `functional-programming`. This ensures the generated file is named `functional-programming.jar` instead of the default `functional-programming-1.0-SNAPSHOT.jar`, making the run command cleaner.

```xml
<build>
  <finalName>functional-programming</finalName>
  <!-- ... plugins ... -->
</build>
```
