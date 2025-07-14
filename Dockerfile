# Etapa 1: Compilar usando el Gradle Wrapper del proyecto
FROM azul/zulu-openjdk-alpine:21 AS builder

# Instala bash, necesarias para ejecutar ./gradlew
RUN apk add --no-cache bash

WORKDIR /app

# Copia primero los archivos del wrapper para aprovechar el cache
COPY gradlew .
COPY gradle gradle

# Da permisos de ejecución al gradlew
RUN chmod +x gradlew

# Copia el resto del proyecto
COPY . .

# Compila con el wrapper
RUN ./gradlew build --no-daemon

# Etapa 2: Imagen liviana solo con JRE para ejecutar
FROM azul/zulu-openjdk-alpine:21-jre-latest

WORKDIR /app

# Copia el JAR generado desde la etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]