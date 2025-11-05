FROM eclipse-temurin:17-jre
ARG JAR_FILE=./build/libs/lunar-calendar-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]