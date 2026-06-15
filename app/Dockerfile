FROM eclipse-temurin:17-jre

WORKDIR /usr/src/app

# The JAR is produced by the pipeline's Package stage (mvn package)
# before this image is built, so we just copy it in and run it.
COPY target/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
