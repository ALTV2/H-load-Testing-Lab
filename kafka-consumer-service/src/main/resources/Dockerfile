
# Используем официальный образ Maven для сборки приложения
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
#COPY pom.xml .
#COPY src ./src
COPY target ./target

# Собираем приложение
#RUN chmod 777 /app/src/main/resources/openapi/dean-office-service-openapi.yaml
#RUN mvn clean package -DskipTests

# Используем официальный образ OpenJDK для запуска приложения
FROM eclipse-temurin:21-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

#RUN chmod 777 app.jar
# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8081/tcp
