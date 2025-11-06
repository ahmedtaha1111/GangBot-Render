FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

# بناء الجار بالـ JAR اللي عندك جاهز
CMD ["java", "-jar", "JDA-6.0.0-withDependencies-min.jar"]