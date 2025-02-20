
# Используем официальный образ Maven для сборки приложения
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

COPY target ./target

# Используем официальный образ OpenJDK для запуска приложения
FROM eclipse-temurin:21-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080/tcp
