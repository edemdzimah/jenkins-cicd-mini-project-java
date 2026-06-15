# Build Tools by Language: When Maven, Gradle, and pip Are Used

A short reference to answer a common question: why does the Python project only install dependencies in its `Build` stage, while a Java project actually compiles code? The answer is that the build tool depends on the language.

## What "build" means

In a pipeline, the `Build` stage prepares your code to run or ship. What that involves depends on whether the language is interpreted or compiled.

- **Interpreted languages** (Python, JavaScript, Ruby): there is no compile step. The source runs as-is. "Build" usually means installing dependencies and sometimes bundling files.
- **Compiled languages** (Java, C#, Go, Rust): "build" means turning source code into something a machine or runtime can execute (bytecode or a binary), then packaging it into an artifact such as a `.jar` or an executable.

This is exactly why the Python project's `Build` stage only runs `pip install`, while the Java project's `Build` stage runs a real compile with Maven.

## The build tool depends on the language

| Language | Typical build tool | What it does | Example Build stage command |
| --- | --- | --- | --- |
| Python | `pip` + `venv` (or Poetry) | install dependencies | `pip install -r requirements.txt` |
| Java | Maven or Gradle | compile, test, package a JAR | `mvn package` or `gradle build` |
| JavaScript / Node | `npm` or `yarn` | install deps, bundle | `npm ci && npm run build` |
| Go | `go` (built in) | compile to a binary | `go build ./...` |
| C# / .NET | `dotnet` | restore, build, publish | `dotnet build` |
| Rust | Cargo | compile, manage deps | `cargo build --release` |

## When Maven and when Gradle (Java)

Maven and Gradle are both Java build tools. Either one will compile your code, run your tests, manage your dependencies, and produce a JAR or WAR. You choose one per project, not both.

- **Maven** is configured in a `pom.xml` file written in XML. It is convention-heavy, extremely common in enterprises, and predictable. It is the easier of the two to read when you are learning.
- **Gradle** is configured in a `build.gradle` file written as a Groovy or Kotlin script. It is more flexible and is usually faster on large projects because of incremental builds and caching. It is the standard for Android.

Rule of thumb: if you are learning, or joining an enterprise Java team, Maven is the safe default. Reach for Gradle when you need custom build logic or faster builds on a large codebase. The Java version of this project uses Maven for that reason.

## The shape of the pipeline never changes

This is the key idea. Whatever the language, the stages stay the same: get the code, build it, test it, package it, then deliver it. Only the commands inside each stage change.

- Python `Build`: `pip install -r requirements.txt`
- Java `Build`: `mvn -B clean compile`
- Node `Build`: `npm ci`

Learn the shape of a pipeline once, and you can apply it to any stack by swapping `pip` for `mvn`, `gradle`, `npm`, or `go`. The tool changes; the thinking does not.
