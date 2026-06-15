# CI/CD Mini Project: Jenkins Pipeline (Java + Maven)

This is the Java version of the CI/CD mini project. It does the same job as the Python version, but the app is written in Java and built with Maven. Use it to see what changes when the language is compiled rather than interpreted.

If you have not read it yet, open `BUILD-TOOLS.md` first. It explains why Java has a real compile and package step that Python does not.

## How this differs from the Python version

The shape of the pipeline is the same. Only the build tool and the commands change.

| | Python version | Java version |
| --- | --- | --- |
| Build tool | `pip` + `venv` | Maven (`mvn`) |
| Build stage does | install dependencies | compile the code |
| Extra stage | none | a `Package` stage that creates a JAR |
| Artifact | the source itself | a compiled `app.jar` |
| Pipeline stages | 4 | 5 (a separate `Package` step) |

The extra `Package` stage exists because Java must be compiled and bundled into a JAR before it can run. Python skipped this because it runs the source directly.

## Project structure

```
jenkins-cicd-mini-project-java/
├── app/
│   ├── pom.xml                              # Maven build file (deps, plugins, JAR config)
│   ├── Dockerfile                           # Runs the built JAR on a slim JRE
│   └── src/
│       ├── main/java/guru/elevatehub/
│       │   ├── App.java                     # Entry point (main)
│       │   └── Calculator.java              # The code under test
│       └── test/java/guru/elevatehub/
│           └── CalculatorTest.java          # JUnit 5 tests
├── Jenkinsfile                              # The 5-stage pipeline
├── jenkins/
│   ├── Dockerfile                           # Jenkins image with Maven + Docker CLI
│   └── docker-compose.yml                   # Starts Jenkins and a Docker daemon
├── BUILD-TOOLS.md                           # When Maven, Gradle, pip are used
└── README.md                               # This file
```

## Prerequisites

- Docker and Docker Compose.
- A GitHub account and a repository for this project.
- About 60 to 90 minutes.

If you also did the Python project, stop its Jenkins first with `docker compose down` in that project's `jenkins/` folder, so the two do not fight over port 8080.

## Step 0: Run the build and tests on your own machine first (optional)

If you have Maven installed locally, do the manual version once so you know what the pipeline automates:

```bash
cd app
mvn clean compile     # compile the code
mvn test              # run the tests
mvn package           # build target/app.jar
java -jar target/app.jar
```

If you do not have Maven locally, skip this. Jenkins has Maven built into its image and will do it for you.

## Step 1: Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit: Java CI/CD mini project"
git branch -M main
git remote add origin https://github.com/<your-username>/<your-repo>.git
git push -u origin main
```

## Step 2: Start Jenkins

```bash
cd jenkins
docker compose up -d --build
```

Get the one-time unlock password:

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Open `http://localhost:8080`, paste the password, choose Install suggested plugins, and create your admin user.

## Step 3: Create the pipeline job

1. New Item, name it `cicd-java`, choose Pipeline, click OK.
2. Under Pipeline, set Definition to Pipeline script from SCM.
3. Set SCM to Git, paste your repository URL.
4. Set Branch Specifier to `*/main`, leave Script Path as `Jenkinsfile`.
5. Save.

## Step 4: Run it

Click Build Now and watch the five stages. The first run is slower because Maven downloads its dependencies. A green run means your code compiled, tests passed, the JAR was packaged, and the Docker image was built.

Confirm the image exists:

```bash
docker exec jenkins-docker docker images | grep cicd-mini-project-java
```

## Understanding the Jenkinsfile

- `Checkout` pulls your code.
- `Build` runs `mvn -B -ntp clean compile`. This is the real difference from Python: the code is compiled into Java bytecode.
- `Test` runs `mvn -B -ntp test`, then publishes the JUnit reports Maven writes to `target/surefire-reports`.
- `Package` runs `mvn -B -ntp package -DskipTests` to produce `target/app.jar`. Tests are skipped here because the `Test` stage already ran them.
- `Docker Build` copies that JAR into a small JRE image.

The flags `-B` (batch mode) and `-ntp` (no transfer progress) just keep the logs clean in Jenkins.

## Exercises

1. **Break a test.** Change an expected value in `CalculatorTest.java`, push, and watch the `Test` stage fail and `Package` and `Docker Build` get skipped. Then fix it.
2. **Add a feature with a test.** Add a `multiply(int a, int b)` method to `Calculator`, write a JUnit test for it, push, and watch the pipeline validate it.
3. **Try a real failure mode.** Introduce a compile error (remove a semicolon), push, and notice the pipeline fails at the `Build` stage, not the `Test` stage. This is a failure Python could never show you, because Python does not compile.

## Troubleshooting

- **`mvn: command not found` in the pipeline.** The Jenkins image build did not finish. Rerun `docker compose up -d --build` in the `jenkins/` folder.
- **First build is very slow.** Maven is downloading dependencies into the Jenkins container. This is normal once; later builds are faster.
- **`Cannot connect to the Docker daemon`.** The `jenkins-docker` container is not ready. Confirm both containers are up and rerun the build.
- **Port 8080 already in use.** Another Jenkins (likely the Python project) is still running. Stop it with `docker compose down` in that project first.

## Cleanup

```bash
cd jenkins
docker compose down        # stop Jenkins, keep data
docker compose down -v     # stop and wipe data for a clean reset
```
