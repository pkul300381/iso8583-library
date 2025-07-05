# iso8583-library

This is a small library demonstrating how to parse ISO 8583 messages in Java.
It includes a simple parser that handles a limited set of fixed length fields
and falls back to LLVAR for other fields.

## Building

The project uses Maven. Run the following command to compile the sources:

```bash
mvn package
```

This will produce a jar file under `target/` that contains all dependencies.

### Running tests

If your environment restricts access to Maven Central, install the required
Maven plugins from your package manager and run tests using the system
repository:

```bash
mvn -Dmaven.repo.local=/usr/share/maven-repo test
```


## Running the example

After building, you can run the included example application which parses a
hardcoded message and prints the JSON representation:

```bash
java -jar target/iso8583-library-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## License

This project is licensed under the terms of the MIT license.
